package Main;

import Data.*;
import Download.CDSParser;
import Download.GenbankCDS;
import Download.GenbankOrganisms;
import Download.OrganismParser;
import Excel.ExcelWriter;
import Exception.*;
import GUI.MainFrame;
import Manager.ITask;
import Manager.ThreadManager;
import Utils.Logs;
import Utils.Options;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Activity {

    private static final Lock m_lock = new ReentrantLock();
    private static final Condition m_cond = m_lock.newCondition();
    private static final Object s_stopLock = new Object();
    private static final Object s_runLock = new Object();
    private static Boolean s_stop = false;
    private static Boolean s_run = false;
    private static Thread s_activityThread = null;
    private static boolean s_wait = false;

    private static String getDifference(Date _startDate, Date _endDate) {
        long different = _endDate.getTime() - _startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return elapsedDays + " day " + elapsedHours + " hours " + elapsedMinutes + " minutes " + elapsedSeconds + " second";
    }

    private static Kingdom switchKingdom(Kingdom _currentKingdom, String _newKingdom, DataBase _parent) throws InvalidStateException, AddException {
        _currentKingdom.stop();
        _currentKingdom = Kingdom.load(_newKingdom, _parent, _kingdom -> {
            try {
                ExcelWriter.writeKingdom(_kingdom);
                _kingdom.save();
                MainFrame.getSingleton().updateTree(_kingdom.getSavedName() + Options.getSerializeExtension());
            } catch (IOException e) {
                Logs.warning("Unable to write excel kingdom file : " + _kingdom.getName());
                Logs.exception(e);
            }
        });
        _currentKingdom.start();
        return _currentKingdom;
    }

    private static Group switchGroup(Group _currentGroup, String _newGroup, Kingdom _parent) throws InvalidStateException, AddException {
        _currentGroup.stop();
        _currentGroup = Group.load(_newGroup, _parent, _group -> {
            try {
                ExcelWriter.writeGroup(_group);
                _group.save();
                MainFrame.getSingleton().updateTree(_group.getSavedName() + Options.getSerializeExtension());
            } catch (IOException e) {
                Logs.warning("Unable to write excel group file : " + _group.getName());
                Logs.exception(e);
            }
        });
        _currentGroup.start();
        return _currentGroup;
    }

    private static SubGroup switchSubGroup(SubGroup _currentSubGroup, String _newSubGroup, Group _parent) throws InvalidStateException, AddException {
        _currentSubGroup.stop();
        _currentSubGroup = SubGroup.load(_newSubGroup, _parent, _subGroup -> {
            try {
                ExcelWriter.writeSubGroup(_subGroup);
                _subGroup.save();
                MainFrame.getSingleton().updateTree(_subGroup.getSavedName() + Options.getSerializeExtension());
            } catch (IOException e) {
                Logs.warning("Unable to write excel subGroup file : " + _subGroup.getName());
                Logs.exception(e);
            }
        });
        _currentSubGroup.start();
        return _currentSubGroup;
    }

    public static boolean genbank() {
        boolean run = true;
        synchronized (s_runLock) {
            if (!s_run) {
                run = false;
                s_run = true;
            }
        }
        if (!run) {
            m_lock.lock();
            {
                if (s_wait) {
                    s_wait = false;
                }
            }
            m_lock.unlock();
            synchronized (s_stopLock) {
                if (s_stop) {
                    s_stop = false;
                }
            }
            MainFrame.getSingleton().updateProgresseValue(0);
            s_activityThread = new Thread(() -> {
                Date beg = new Date();
                final ThreadManager threadManager = new ThreadManager(Runtime.getRuntime().availableProcessors() * 4);
                final int[] index = {0};
                try {
                    final GenbankOrganisms go = new GenbankOrganisms();
                    go.downloadOrganisms();
                    MainFrame.getSingleton().updateProgresseMax(go.getTotalCount() + 1);

                    final DataBase currentDataBase = DataBase.load(Options.getGenbankName(), _dataBase -> {
                        try {
                            ExcelWriter.writeDatabase(_dataBase);
                            _dataBase.save();
                            MainFrame.getSingleton().updateTree(_dataBase.getSavedName() + Options.getSerializeExtension());
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

                    final Lock m_indexLock = new ReentrantLock();
                    while (go.hasNext()) {
                        m_lock.lock();
                        {
                            while (s_wait) {
                                Logs.info("wait...", true);
                                try {
                                    m_cond.await();
                                } catch (InterruptedException e) {
                                    Logs.exception(e);
                                }
                            }
                        }
                        m_lock.unlock();
                        synchronized (s_stopLock) {
                            if (s_stop) {
                                break;
                            }
                        }
                        final OrganismParser organismParser = go.getNext();
                        final String organismName = organismParser.getName() + "-" + organismParser.getId();

                        final Date dateModif = Organism.loadDate(Options.getGenbankName(), organismParser.getKingdom(), organismParser.getGroup(), organismParser.getSubGroup(), organismName);
                        if (dateModif != null && organismParser.getModificationDate().compareTo(dateModif) <= 0) {
                            Logs.info("Organism " + organismName + " already up to date", false);
                            m_indexLock.lock();
                            {
                                MainFrame.getSingleton().updateProgresseValue(++index[0]);
                            }
                            m_indexLock.unlock();
                            continue;
                        }
                        if (organismParser.getReplicons().size() == 0) {
                            Logs.info("No replicon in : " + organismName, false);
                            m_indexLock.lock();
                            {
                                MainFrame.getSingleton().updateProgresseValue(++index[0]);
                            }
                            m_indexLock.unlock();
                            continue;
                        }

                        if (organismParser.getKingdom().compareTo(currentKingdom.getName()) != 0) {
                            currentKingdom = switchKingdom(currentKingdom, organismParser.getKingdom(), currentDataBase);
                            currentGroup = switchGroup(currentGroup, organismParser.getGroup(), currentKingdom);
                            currentSubGroup = switchSubGroup(currentSubGroup, organismParser.getSubGroup(), currentGroup);
                        } else if (organismParser.getGroup().compareTo(currentGroup.getName()) != 0) {
                            currentGroup = switchGroup(currentGroup, organismParser.getGroup(), currentKingdom);
                            currentSubGroup = switchSubGroup(currentSubGroup, organismParser.getSubGroup(), currentGroup);
                        } else if (organismParser.getSubGroup().compareTo(currentSubGroup.getName()) != 0) {
                            currentSubGroup = switchSubGroup(currentSubGroup, organismParser.getSubGroup(), currentGroup);
                        }

                        Organism organism = Organism.load(organismName, organismParser.getId(), organismParser.getVersion(), currentSubGroup, true, _organism -> {
                            try {
                                ExcelWriter.writeOrganism(_organism);
                                _organism.save();
                                MainFrame.getSingleton().updateTree(_organism.getSavedName() + Options.getSerializeExtension());
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
                                    m_indexLock.lock();
                                    {
                                        MainFrame.getSingleton().updateProgresseValue(++index[0]);
                                    }
                                    m_indexLock.unlock();
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
                } finally {
                    Logs.info("Finished and wait for threads...", true);
                    threadManager.finalizeThreadManager();
                    synchronized (s_runLock) {
                        s_run = false;
                    }
                    MainFrame.getSingleton().updateProgresseValue(++index[0]);
                    Date end = new Date();
                    Logs.info("Execution time : " + getDifference(beg, end), true);
                }
            });
            s_activityThread.start();
            return true;
        }
        return false;
    }

    public static boolean stop() {
        Logs.info("stop requested ...", true);
        boolean ret = false;
        synchronized (s_stopLock) {
            if (!s_stop) {
                s_stop = true;
                ret = true;
            }
        }
        m_lock.lock();
        {
            if (s_wait) {
                s_wait = false;
                m_cond.signalAll();
            }
        }
        m_lock.unlock();
        return ret;
    }

    public static void stopAndWait() {
        stop();
        if (s_activityThread != null) {
            try {
                s_activityThread.join();
            } catch (InterruptedException e) {
                Logs.exception(e);
            }
        }
    }

    public static boolean pause() {
        Logs.info("pause requested ...", true);
        boolean ret = false;
        m_lock.lock();
        {
            if (!s_wait) {
                s_wait = true;
                ret = true;
            }
        }
        m_lock.unlock();
        return ret;
    }

    public static boolean resume() {
        Logs.info("resume requested ...", true);
        boolean ret = false;
        m_lock.lock();
        {
            if (s_wait) {
                s_wait = false;
                m_cond.signalAll();
                ret = true;
            }
        }
        m_lock.unlock();
        return ret;
    }

}
