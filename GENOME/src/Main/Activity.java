package Main;

import Data.*;
import Download.CDSParser;
import Download.GenbankCDS;
import Download.GenbankOrganisms;
import Download.OrganismParser;
import Excel.ExcelWriter;
import Exception.*;
import Json.JSONException;
import Manager.ITask;
import Manager.ThreadManager;
import Utils.Logs;
import Utils.Options;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class Activity {

    private static Kingdom switchKingdom(Kingdom _currentKingdom, String _newKingdom, DataBase _parent, ActivityListener _activityListener) throws InvalidStateException, AddException {
        _currentKingdom.stop();
        _currentKingdom = Kingdom.load(_newKingdom, _parent, _kingdom -> {
            try {
                ExcelWriter.writeKingdom(_kingdom);
                _kingdom.save();
                _activityListener.ActivityEvent(_kingdom.getSavedName() + Options.getSerializeExtension());
            } catch (IOException e) {
                Logs.warning("Unable to write excel kingdom file : " + _kingdom.getName());
                Logs.exception(e);
            }
        });
        _currentKingdom.start();
        return _currentKingdom;
    }

    private static Group switchGroup(Group _currentGroup, String _newGroup, Kingdom _parent, ActivityListener _activityListener) throws InvalidStateException, AddException {
        _currentGroup.stop();
        _currentGroup = Group.load(_newGroup, _parent, _group -> {
            try {
                ExcelWriter.writeGroup(_group);
                _group.save();
                _activityListener.ActivityEvent(_group.getSavedName() + Options.getSerializeExtension());
            } catch (IOException e) {
                Logs.warning("Unable to write excel group file : " + _group.getName());
                Logs.exception(e);
            }
        });
        _currentGroup.start();
        return _currentGroup;
    }

    private static SubGroup switchSubGroup(SubGroup _currentSubGroup, String _newSubGroup, Group _parent, ActivityListener _activityListener) throws InvalidStateException, AddException {
        _currentSubGroup.stop();
        _currentSubGroup = SubGroup.load(_newSubGroup, _parent, _subGroup -> {
            try {
                ExcelWriter.writeSubGroup(_subGroup);
                _subGroup.save();
                _activityListener.ActivityEvent(_subGroup.getSavedName() + Options.getSerializeExtension());
            } catch (IOException e) {
                Logs.warning("Unable to write excel subGroup file : " + _subGroup.getName());
                Logs.exception(e);
            }
        });
        _currentSubGroup.start();
        return _currentSubGroup;
    }

    public static void genbank(ActivityListener _activityListener) throws InvalidStateException, AddException, MissException {
        final ThreadManager threadManager = new ThreadManager(Runtime.getRuntime().availableProcessors() * 4);
        try {
            final GenbankOrganisms go = new GenbankOrganisms();
            go.downloadOrganisms();

            final DataBase currentDataBase = DataBase.load(Options.getGenbankName(), _dataBase -> {
                try {
                    ExcelWriter.writeDatabase(_dataBase);
                    _dataBase.save();
                    _activityListener.ActivityEvent(_dataBase.getSavedName() + Options.getSerializeExtension());
                } catch (IOException e) {
                    Logs.warning("Unable to write excel database file : " + _dataBase.getName());
                    Logs.exception(e);
                }
            });
            currentDataBase.start();

            Kingdom currentKingdom = Kingdom.load("", currentDataBase, _kingdom -> {
            });
            currentKingdom.start();

            Group currentGroup = Group.load("", currentKingdom, _group -> {
            });
            currentGroup.start();

            SubGroup currentSubGroup = SubGroup.load("", currentGroup, _subGroup -> {
            });
            currentSubGroup.start();

            while (go.hasNext()) {
                final OrganismParser organismParser = go.getNext();
                try {
                    organismParser.parse();
                } catch (JSONException e) {
                    Logs.warning("Unable to parse : " + organismParser.getId() + ", " + organismParser.getName());
                    Logs.exception(e);
                    continue;
                }

                final String organismName = organismParser.getName() + "-" + organismParser.getId();

                final Date dateModif = Organism.loadDate(Options.getGenbankName(), organismParser.getKingdom(), organismParser.getGroup(), organismParser.getSubGroup(), organismName);
                if (dateModif != null && organismParser.getModificationDate().compareTo(dateModif) <= 0) {
                    Logs.info("Organism " + organismName + " already up to date");
                    continue;
                }
                if (organismParser.getReplicons().size() == 0) {
                    Logs.info("No replicon in : " + organismName);
                    continue;
                }

                if (organismParser.getKingdom().compareTo(currentKingdom.getName()) != 0) {
                    currentKingdom = switchKingdom(currentKingdom, organismParser.getKingdom(), currentDataBase, _activityListener);
                    currentGroup = switchGroup(currentGroup, organismParser.getGroup(), currentKingdom, _activityListener);
                    currentSubGroup = switchSubGroup(currentSubGroup, organismParser.getSubGroup(), currentGroup, _activityListener);
                } else if (organismParser.getGroup().compareTo(currentGroup.getName()) != 0) {
                    currentGroup = switchGroup(currentGroup, organismParser.getGroup(), currentKingdom, _activityListener);
                    currentSubGroup = switchSubGroup(currentSubGroup, organismParser.getSubGroup(), currentGroup, _activityListener);
                } else if (organismParser.getSubGroup().compareTo(currentSubGroup.getName()) != 0) {
                    currentSubGroup = switchSubGroup(currentSubGroup, organismParser.getSubGroup(), currentGroup, _activityListener);
                }

                Organism organism = Organism.load(organismName, organismParser.getId(), organismParser.getVersion(), currentSubGroup, true, _organism -> {
                    try {
                        ExcelWriter.writeOrganism(_organism);
                        _organism.save();
                        _activityListener.ActivityEvent(_organism.getSavedName() + Options.getSerializeExtension());
                    } catch (IOException e) {
                        Logs.warning("Unable to write excel file : " + _organism.getName());
                        Logs.exception(e);
                    }
                });

                // Thread
                threadManager.pushTask(new ITask(organismName) {
                    @Override
                    public void run() {
                        try {
                            try {
                                organism.start();
                            } catch (InvalidStateException e) {
                                Logs.warning("Unable to start : " + organism.getName());
                                Logs.exception(e);
                                return;
                            }
                            for (Map.Entry<String, String> ent : organismParser.getReplicons()) {
                                final GenbankCDS cdsDownloader = new GenbankCDS(ent.getKey());
                                try {
                                    cdsDownloader.download();
                                } catch (HTTPException | IOException | OutOfMemoryException e) {
                                    Logs.warning("Unable to download : " + ent.getKey());
                                    Logs.exception(e);
                                    continue;
                                }
                                final CDSParser cdsParser = new CDSParser(cdsDownloader.getRefseqData(), ent.getKey());
                                try {
                                    cdsParser.parse();
                                } catch (OperatorException e) {
                                    Logs.warning("Unable to parse : " + ent.getKey());
                                    Logs.exception(e);
                                    continue;
                                }

                                final Replicon replicon = new Replicon(Statistics.Type.isTypeOf(ent.getValue()), ent.getKey(), cdsParser.getTotal(), cdsParser.getValid(), cdsParser.getSequences());
                                try {
                                    organism.addReplicon(replicon);
                                } catch (AddException e) {
                                    Logs.warning("Unable to add replicon : " + replicon.getName());
                                    Logs.exception(e);
                                }
                            }
                        } catch (OutOfMemoryError e) {
                            Logs.warning("Memory error : " + organism.getName());
                            Logs.exception(new Exception(e));
                        } catch (Throwable e) {
                            Logs.warning("Unknow error : " + organism.getName());
                            Logs.exception(new Exception(e));
                        } finally {
                            try {
                                organism.stop();
                            } catch (InvalidStateException e) {
                                Logs.warning("Unable to stop : " + organism.getName());
                                Logs.exception(e);
                            }
                            try {
                                organism.finish();
                            } catch (InvalidStateException e) {
                                Logs.warning("Unable to finish : " + organism.getName());
                                Logs.exception(e);
                            }
                        }
                    }
                });
            }

            currentDataBase.stop();
            currentKingdom.stop();
            currentGroup.stop();
            currentSubGroup.stop();
        } catch (InvalidStateException | AddException | MissException e) {
            Logs.warning("Unable to run programme");
            Logs.exception(e);
            throw e;
        } finally {
            threadManager.finalizeThreadManager();
        }
    }

    public interface ActivityListener {
        void ActivityEvent(String _message);
    }
}
