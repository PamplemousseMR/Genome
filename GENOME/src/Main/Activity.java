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

import java.io.IOException;
import java.util.Map;

public class Activity {

    public static void genbank() throws Exception {
        final GenbankOrganisms go = new GenbankOrganisms();
        go.downloadOrganisms();

        final ThreadManager threadManager = new ThreadManager(Runtime.getRuntime().availableProcessors() * 4);

        final DataBase currentDataBase = new DataBase("Genbank", _dataBase -> {
            try {
                ExcelWriter.writeDatabase(_dataBase);
            } catch (IOException e) {
                Logs.warning("Unable to write excel database file : " + _dataBase.getName());
                Logs.exception(e);
            }
        });
        Kingdom currentKingdom = new Kingdom("", _kingdom -> {
        });
        Group currentGroup = new Group("", _group -> {
        });
        SubGroup currentSubGroup = new SubGroup("", _subGroup -> {
        });

        currentDataBase.start();
        currentKingdom.start();
        currentGroup.start();
        currentSubGroup.start();

        currentGroup.addSubGroup(currentSubGroup);
        currentKingdom.addGroup(currentGroup);
        currentDataBase.addKingdom(currentKingdom);

        while (go.hasNext()) {
            final OrganismParser organismParser = go.getNext();
            try {
                organismParser.parse();
            } catch (JSONException e) {
                Logs.warning("Unable to parse : " + organismParser.getId() + ", " + organismParser.getName());
                Logs.exception(e);
                continue;
            }

            if (organismParser.getReplicons().size() == 0) {
                Logs.info("No replicon in : " + organismParser.getId() + ", " + organismParser.getName());
                continue;
            }

            if (organismParser.getKingdom().compareTo(currentKingdom.getName()) != 0) {
                currentKingdom.stop();
                currentGroup.stop();
                currentSubGroup.stop();

                currentKingdom = new Kingdom(organismParser.getKingdom(), _kingdom -> {
                    try {
                        ExcelWriter.writeKingdom(_kingdom);
                    } catch (IOException e) {
                        Logs.warning("Unable to write excel kingdom file : " + _kingdom.getName());
                        Logs.exception(e);
                    }
                });
                currentGroup = new Group(organismParser.getGroup(), _group -> {
                    try {
                        ExcelWriter.writeGroup(_group);
                    } catch (IOException e) {
                        Logs.warning("Unable to write excel group file : " + _group.getName());
                        Logs.exception(e);
                    }
                });
                currentSubGroup = new SubGroup(organismParser.getSubGroup(), _subGroup -> {
                    try {
                        ExcelWriter.writeSubGroup(_subGroup);
                    } catch (IOException e) {
                        Logs.warning("Unable to write excel subgroup file : " + _subGroup.getName());
                        Logs.exception(e);
                    }
                });

                currentKingdom.start();
                currentGroup.start();
                currentSubGroup.start();

                currentDataBase.addKingdom(currentKingdom);
                currentKingdom.addGroup(currentGroup);
                currentGroup.addSubGroup(currentSubGroup);
            } else if (organismParser.getGroup().compareTo(currentGroup.getName()) != 0) {
                currentGroup.stop();
                currentSubGroup.stop();

                currentGroup = new Group(organismParser.getGroup(), _group -> {
                    try {
                        ExcelWriter.writeGroup(_group);
                    } catch (IOException e) {
                        Logs.warning("Unable to write excel group file : " + _group.getName());
                        Logs.exception(e);
                    }
                });
                currentSubGroup = new SubGroup(organismParser.getSubGroup(), _subGroup -> {
                    try {
                        ExcelWriter.writeSubGroup(_subGroup);
                    } catch (IOException e) {
                        Logs.warning("Unable to write excel subgroup file : " + _subGroup.getName());
                        Logs.exception(e);
                    }
                });

                currentGroup.start();
                currentSubGroup.start();

                currentKingdom.addGroup(currentGroup);
                currentGroup.addSubGroup(currentSubGroup);
            } else if (organismParser.getSubGroup().compareTo(currentSubGroup.getName()) != 0) {
                currentSubGroup.stop();

                currentSubGroup = new SubGroup(organismParser.getSubGroup(), _subGroup -> {
                    try {
                        ExcelWriter.writeSubGroup(_subGroup);
                    } catch (IOException e) {
                        Logs.warning("Unable to write excel subgroup file : " + _subGroup.getName());
                        Logs.exception(e);
                    }
                });

                currentSubGroup.start();

                currentGroup.addSubGroup(currentSubGroup);
            }
            Organism organism = new Organism(organismParser.getName(), organismParser.getId(), organismParser.getVersion(), _organism -> {
                try {
                    ExcelWriter.writeOrganism(_organism);
                } catch (IOException e) {
                    Logs.warning("Unable to write excel file : " + _organism.getName());
                    Logs.exception(e);
                }
            });
            currentSubGroup.addOrganism(organism);

            // Thread
            threadManager.pushTask(new ITask(organismParser.getName()) {
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

        threadManager.finalizeThreadManager();
    }

}
