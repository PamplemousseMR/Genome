package Main;

import Data.*;
import Download.CDSParser;
import Download.GenbankCDS;
import Download.GenbankOrganisms;
import Download.OrganismParser;
import Excel.ExcelWriter;
import Exception.*;
import Manager.ITask;
import Manager.ThreadManager;
import Utils.Logs;
import Utils.Options;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class GenomeConsoleActivity {

    private static final Lock s_WAIT_LOCK = new ReentrantLock();
    private static final Condition s_COND = s_WAIT_LOCK.newCondition();
    private static final Object s_STOP_LOCK = new Object();
    private static final Object s_RUN_LOCK = new Object();
    private static Boolean s_stop = false;
    private static Boolean s_run = false;
    private static Thread s_activityThread = null;
    private static boolean s_wait = false;

    /**
     * Run main activity
     *
     * @return true if activity is started
     */
    public static boolean genbank() {
        boolean run = true;
        synchronized (s_RUN_LOCK) {
            if (!s_run) {
                run = false;
                s_run = true;
            }
        }
        if (!run) {
            Logs.notice("Start", true);
            s_WAIT_LOCK.lock();
            {
                if (s_wait) {
                    s_wait = false;
                }
            }
            s_WAIT_LOCK.unlock();
            synchronized (s_STOP_LOCK) {
                if (s_stop) {
                    s_stop = false;
                }
            }
            s_activityThread = new Thread(() -> {
                Date beg = new Date();
                final int[] fail = {0};
                final int[] index = {0};
                boolean cancel = false;
                ThreadManager threadManager = new ThreadManager(Runtime.getRuntime().availableProcessors() * 4);
                try {
                    // Get existing files
                    final File path = new File(Options.getSerializeDirectory());
                    ArrayList<File> files = new ArrayList<>();
                    if (path.exists()) {
                        File[] f = path.listFiles();
                        if (f != null) {
                            files = new ArrayList<>(Arrays.asList(f));
                            String regex = "^" + Options.getDatabaseSerializationPrefix() + "[^-]*" + Options.getSerializationSpliter() +
                                    Options.getKingdomSerializationPrefix() + "[^-]*" + Options.getSerializationSpliter() +
                                    Options.getGroupSerializationPrefix() + "[^-]*" + Options.getSerializationSpliter() +
                                    Options.getSubGroupSerializationPrefix() + "[^-]*" + Options.getSerializationSpliter() +
                                    Options.getOrganismSerializationPrefix() + "[^-]*-[^-]*" + Options.getSerializeExtension() + "$";
                            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                            files.removeIf(file -> {
                                Matcher m = pattern.matcher(file.getName());
                                return !m.find();
                            });
                            // Sort local files with the same method than the GenbankOrganism
                            files.sort((_o1, _o2) -> {
                                String[] table1 = _o1.getName().split(Options.getSerializationSpliter());
                                String kingdom1 = table1[1].substring(2);
                                String group1 = table1[2].substring(2);
                                String subGroup1 = table1[3].substring(3);
                                String organism1 = table1[4].substring(2);

                                String[] table2 = _o2.getName().split(Options.getSerializationSpliter());
                                String kingdom2 = table2[1].substring(2);
                                String group2 = table2[2].substring(2);
                                String subGroup2 = table2[3].substring(3);
                                String organism2 = table2[4].substring(2);

                                int k = kingdom1.compareTo(kingdom2);
                                if (k == 0) {
                                    int g = group1.compareTo(group2);
                                    if (g == 0) {
                                        int s = subGroup1.compareTo(subGroup2);
                                        if (s == 0) {
                                            return organism1.compareTo(organism2);
                                        } else {
                                            return s;
                                        }
                                    } else {
                                        return g;
                                    }
                                } else {
                                    return k;
                                }
                            });
                        }
                    }
                    Iterator<File> filesIt = files.iterator();

                    // Get organisms list
                    final GenbankOrganisms go = new GenbankOrganisms();
                    go.downloadOrganisms();

                    final DataBase currentDataBase = DataBase.load(Options.getGenbankName(), _dataBase -> {
                        try {
                            ExcelWriter.writeDatabase(_dataBase);
                        } catch (IOException | NoClassDefFoundError e) {
                            Logs.warning("Unable to write excel database file : " + _dataBase.getName());
                            Logs.exception(e);
                        }
                        _dataBase.save();
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

                    // Each organism
                    String currentLocalFileName = null;
                    if (filesIt.hasNext()) {
                        currentLocalFileName = filesIt.next().getName();
                    }
                    final Object m_indexLock = new Object();
                    while (go.hasNext()) {
                        wait(GenomeActivity.class.toString());
                        synchronized (s_STOP_LOCK) {
                            if (s_stop) {
                                Logs.notice("Stop main loop", true);
                                cancel = true;
                                break;
                            }
                        }
                        final OrganismParser organismParser = go.getNext();
                        final String organismName = organismParser.getName() + "-" + organismParser.getId();

                        String currentRemoteFileName = DataBase.s_SERIALIZATION_PREFIX + Options.getGenbankName() + Kingdom.s_SERIALIZATION_PREFIX + organismParser.getKingdom() + Group.s_SERIALIZATION_PREFIX + organismParser.getGroup() + SubGroup.s_SERIALIZATION_PREFIX + organismParser.getSubGroup() + Organism.s_SERIALIZATION_PREFIX + organismName + Options.getSerializeExtension();
                        // Delete local files
                        while (currentLocalFileName != null && currentLocalFileName.compareTo(currentRemoteFileName) < 0) {
                            String[] table = currentLocalFileName.split(Options.getSerializationSpliter());
                            String localKingdom = table[1].substring(2);
                            String localGroup = table[2].substring(2);
                            String localSubGroup = table[3].substring(3);
                            String localOrganism = table[4].substring(2);

                            // Check if the subgroup need to be switched
                            if (localKingdom.compareTo(currentKingdom.getName()) != 0) {
                                currentKingdom = switchKingdom(currentKingdom, localKingdom, currentDataBase);
                                currentGroup = switchGroup(currentGroup, localGroup, currentKingdom);
                                currentSubGroup = switchSubGroup(currentSubGroup, localSubGroup, currentGroup);
                            } else if (localGroup.compareTo(currentGroup.getName()) != 0) {
                                currentGroup = switchGroup(currentGroup, localGroup, currentKingdom);
                                currentSubGroup = switchSubGroup(currentSubGroup, localSubGroup, currentGroup);
                            } else if (localSubGroup.compareTo(currentSubGroup.getName()) != 0) {
                                currentSubGroup = switchSubGroup(currentSubGroup, localSubGroup, currentGroup);
                            }

                            // Delete the organism
                            Logs.info("Delete organism : " + localOrganism + ". Current remote : " + organismName, true);
                            deleteOrganism(currentLocalFileName, currentSubGroup);

                            // Get next local file
                            if (filesIt.hasNext()) {
                                currentLocalFileName = filesIt.next().getName();
                            } else {
                                currentLocalFileName = null;
                            }
                        }
                        if (currentLocalFileName != null && currentLocalFileName.compareTo(currentRemoteFileName) == 0) {
                            if (filesIt.hasNext()) {
                                currentLocalFileName = filesIt.next().getName();
                            } else {
                                currentLocalFileName = null;
                            }
                        }

                        // Check if it need to be updated
                        final Date dateModif = Organism.loadDate(Options.getGenbankName(), organismParser.getKingdom(), organismParser.getGroup(), organismParser.getSubGroup(), organismName);
                        if (dateModif != null && organismParser.getModificationDate().compareTo(dateModif) <= 0) {
                            Logs.info("Organism " + organismName + " already up to date", false);
                            continue;
                        }

                        // Check if the subgroup need to be switched
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

                        // Load the organism (if it already exist, unload data from the parent)
                        Organism organism = Organism.load(organismName, organismParser.getId(), organismParser.getVersion(), currentSubGroup, true, _organism -> {
                            try {
                                ExcelWriter.writeOrganism(_organism);
                            } catch (IOException | NoClassDefFoundError e) {
                                Logs.warning("Unable to write excel file : " + _organism.getName());
                                Logs.exception(e);
                            }
                            _organism.save();
                        });

                        // Thread, download CDS
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
                                        GenomeConsoleActivity.wait(getName());
                                        final GenbankCDS cdsDownloader = new GenbankCDS(ent.getKey());
                                        try {
                                            cdsDownloader.download();
                                        } catch (HTTPException | IOException | OutOfMemoryException e) {
                                            Logs.warning("Unable to download : " + ent.getKey());
                                            Logs.exception(e);
                                            throw e;
                                        }
                                        final CDSParser cdsParser = new CDSParser(cdsDownloader.getRefseqData(), ent.getKey());
                                        try {
                                            cdsParser.parse();
                                            if (Options.getSaveGenome()) {
                                                final String path = Options.getGenomeDirectory() + File.separator + organismParser.getKingdom() + File.separator + organismParser.getGroup() + File.separator + organismParser.getSubGroup() + File.separator + organismParser.getName();
                                                cdsParser.saveGenome(path);
                                            }
                                            if (Options.getSaveGene()) {
                                                final String path = Options.getGeneDirectory() + File.separator + organismParser.getKingdom() + File.separator + organismParser.getGroup() + File.separator + organismParser.getSubGroup() + File.separator + organismParser.getName();
                                                cdsParser.saveGene(path);
                                            }
                                        } catch (OperatorException e) {
                                            Logs.warning("Unable to parse : " + ent.getKey());
                                            Logs.exception(e);
                                            throw e;
                                        }

                                        final Replicon replicon = new Replicon(Statistics.Type.isTypeOf(ent.getValue()), ent.getKey(), cdsParser.getTotal(), cdsParser.getValid(), cdsParser.getSequences());
                                        try {
                                            organism.addReplicon(replicon);
                                        } catch (AddException e) {
                                            Logs.warning("Unable to add replicon : " + replicon.getName());
                                            Logs.exception(e);
                                            throw e;
                                        }
                                    }
                                } catch (OutOfMemoryError e) {
                                    Logs.warning("Memory error from organism : " + organism.getName());
                                    Logs.exception(new Exception(e));
                                    try {
                                        organism.cancel();
                                        Logs.info("Cancel organism : " + organism.getName(), true);
                                        synchronized (m_indexLock) {
                                            ++fail[0];
                                        }
                                    } catch (InvalidStateException e1) {
                                        Logs.warning("Unable to cancel : " + organism.getName());
                                        Logs.exception(e);
                                    }
                                } catch (Throwable e) {
                                    Logs.warning("Error from organism : " + organism.getName());
                                    Logs.exception(e);
                                    try {
                                        organism.cancel();
                                        Logs.info("Cancel organism : " + organism.getName(), true);
                                        synchronized (m_indexLock) {
                                            ++fail[0];
                                        }
                                    } catch (InvalidStateException e1) {
                                        Logs.warning("Unable to cancel : " + organism.getName());
                                        Logs.exception(e);
                                    }
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
                                    synchronized (m_indexLock) {
                                    }
                                }
                            }

                            @Override
                            public void cancel() {
                                try {
                                    organism.start();
                                    organism.stop();
                                    organism.cancel();
                                    organism.finish();
                                } catch (InvalidStateException e) {
                                    Logs.warning("Unable to cancel : " + organism.getName());
                                    Logs.exception(e);
                                }
                            }
                        });
                    }
                    // Delete finale local files
                    while (currentLocalFileName != null) {
                        String[] table = currentLocalFileName.split(Options.getSerializationSpliter());
                        String localKingdom = table[1].substring(2);
                        String localGroup = table[2].substring(2);
                        String localSubGroup = table[3].substring(3);
                        String localOrganism = table[4].substring(2);

                        // Check if the subgroup need to be switched
                        if (localKingdom.compareTo(currentKingdom.getName()) != 0) {
                            currentKingdom = switchKingdom(currentKingdom, localKingdom, currentDataBase);
                            currentGroup = switchGroup(currentGroup, localGroup, currentKingdom);
                            currentSubGroup = switchSubGroup(currentSubGroup, localSubGroup, currentGroup);
                        } else if (localGroup.compareTo(currentGroup.getName()) != 0) {
                            currentGroup = switchGroup(currentGroup, localGroup, currentKingdom);
                            currentSubGroup = switchSubGroup(currentSubGroup, localSubGroup, currentGroup);
                        } else if (localSubGroup.compareTo(currentSubGroup.getName()) != 0) {
                            currentSubGroup = switchSubGroup(currentSubGroup, localSubGroup, currentGroup);
                        }

                        // Delete the organism
                        Logs.info("Delete finale organism : " + localOrganism, true);
                        deleteOrganism(currentLocalFileName, currentSubGroup);

                        // Get next local file
                        if (filesIt.hasNext()) {
                            currentLocalFileName = filesIt.next().getName();
                        } else {
                            currentLocalFileName = null;
                        }
                    }

                    currentSubGroup.stop();
                    currentGroup.stop();
                    currentKingdom.stop();
                    currentDataBase.stop();
                } catch (InvalidStateException | AddException | MissException e) {
                    Logs.warning("Unable to run programme");
                    Logs.exception(e);
                } catch (Throwable e) {
                    Logs.warning("Unable to run programme, unexpected error");
                    Logs.exception(e);
                } finally {
                    Logs.notice("Finished and wait for threads...", true);
                    threadManager.finalizeThreadManager(cancel);
                    synchronized (s_RUN_LOCK) {
                        s_run = false;
                    }
                    Date end = new Date();
                    Logs.notice("Execution time : " + getDifference(beg, end), true);
                    Logs.notice("Failled organisms : " + fail[0], true);
                }
            });
            s_activityThread.start();
            return true;
        }
        return false;
    }

    /**
     * Send stop request
     *
     * @return if true if success
     */
    public static boolean stop() {
        boolean ret = false;
        boolean run = false;
        synchronized (s_RUN_LOCK) {
            if (s_run) {
                run = true;
            }
        }
        if (run) {
            synchronized (s_STOP_LOCK) {
                if (!s_stop) {
                    Logs.notice("stop requested ...", true);
                    s_stop = true;
                    ret = true;
                    s_WAIT_LOCK.lock();
                    {
                        if (s_wait) {
                            s_wait = false;
                            s_COND.signalAll();
                        }
                    }
                    s_WAIT_LOCK.unlock();
                }
            }
        }
        return ret;
    }

    /**
     * Send stop request and wait all threads
     */
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

    /**
     * Send pause request
     *
     * @return if true if success
     */
    public static boolean pause() {
        boolean ret = false;
        boolean run = false;
        synchronized (s_RUN_LOCK) {
            if (s_run) {
                run = true;
            }
        }
        if (run) {
            s_WAIT_LOCK.lock();
            {
                if (!s_wait) {
                    Logs.notice("pause requested ...", true);
                    s_wait = true;
                    ret = true;
                }
            }
            s_WAIT_LOCK.unlock();
        }
        return ret;
    }

    /**
     * Send resume request
     *
     * @return if true if success
     */
    public static boolean resume() {
        boolean ret = false;
        boolean run = false;
        synchronized (s_RUN_LOCK) {
            if (s_run) {
                run = true;
            }
        }
        if (run) {
            s_WAIT_LOCK.lock();
            {
                if (s_wait) {
                    Logs.notice("resume requested ...", true);
                    s_wait = false;
                    s_COND.signalAll();
                    ret = true;
                }
            }
            s_WAIT_LOCK.unlock();
        }
        return ret;
    }

    /**
     * Wait if requested
     *
     * @param _name the name of the task
     */
    private static void wait(String _name) {
        s_WAIT_LOCK.lock();
        {
            while (s_wait) {
                Logs.notice(_name + " : wait...", true);
                try {
                    s_COND.await();
                } catch (InterruptedException e) {
                    Logs.exception(e);
                }
            }
        }
        s_WAIT_LOCK.unlock();
    }

    private static void deleteOrganism(String _fileName, SubGroup _subGroup) throws AddException, InvalidStateException {
        String[] table = _fileName.split(Options.getSerializationSpliter());
        String organismName = table[4].substring(2);

        // Load the organism to delete, it will unload data from it's parent
        Organism organism = Organism.load(organismName, 0L, 0L, _subGroup, true, _organism -> {
        });

        // Start it
        try {
            organism.start();
        } catch (InvalidStateException e) {
            Logs.warning("Unable to start : " + organism.getName());
            Logs.exception(e);
            return;
        }

        // Delete local files
        organism.unsave();
        try {
            ExcelWriter.unwriteOrganism(organism);
        } catch (IOException | NoClassDefFoundError e) {
            Logs.warning("Unable to unwrite excel file : " + organism.getName());
            Logs.exception(e);
        }

        // End
        try {
            organism.stop();
            organism.cancel();
            organism.finish();
        } catch (InvalidStateException e) {
            Logs.warning("Unable to cancel : " + organism.getName());
            Logs.exception(e);
        }
    }

    /**
     * Get difference between two date
     *
     * @param _startDate the start date
     * @param _endDate   the and date
     * @return the String displaying the difference
     */
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
            if (_kingdom.getTotalOrganism() == 0) {
                Logs.info("Delete kingdom : " + _kingdom, true);
                _kingdom.unsave();
                try {
                    ExcelWriter.unwriteKingdom(_kingdom);
                } catch (IOException | NoClassDefFoundError e) {
                    Logs.warning("Unable to unwrite excel kingdom file : " + _kingdom.getName());
                    Logs.exception(e);
                }
            } else {
                try {
                    ExcelWriter.writeKingdom(_kingdom);
                } catch (IOException | NoClassDefFoundError e) {
                    Logs.warning("Unable to write excel kingdom file : " + _kingdom.getName());
                    Logs.exception(e);
                }
                _kingdom.save();
            }
        });
        _currentKingdom.start();
        return _currentKingdom;
    }

    private static Group switchGroup(Group _currentGroup, String _newGroup, Kingdom _parent) throws InvalidStateException, AddException {
        _currentGroup.stop();
        _currentGroup = Group.load(_newGroup, _parent, _group -> {
            if (_group.getTotalOrganism() == 0) {
                Logs.info("Delete group : " + _group, true);
                _group.unsave();
                try {
                    ExcelWriter.unwriteGroup(_group);
                } catch (IOException | NoClassDefFoundError e) {
                    Logs.warning("Unable to unwrite excel group file : " + _group.getName());
                    Logs.exception(e);
                }
            } else {
                try {
                    ExcelWriter.writeGroup(_group);
                } catch (IOException | NoClassDefFoundError e) {
                    Logs.warning("Unable to write excel group file : " + _group.getName());
                    Logs.exception(e);
                }
                _group.save();
            }
        });
        _currentGroup.start();
        return _currentGroup;
    }

    private static SubGroup switchSubGroup(SubGroup _currentSubGroup, String _newSubGroup, Group _parent) throws InvalidStateException, AddException {
        _currentSubGroup.stop();
        _currentSubGroup = SubGroup.load(_newSubGroup, _parent, _subGroup -> {
            if (_subGroup.getTotalOrganism() == 0) {
                Logs.info("Delete subGroup : " + _subGroup, true);
                _subGroup.unsave();
                try {
                    ExcelWriter.unwriteSubGroup(_subGroup);
                } catch (IOException | NoClassDefFoundError e) {
                    Logs.warning("Unable to unwrite excel subGroup file : " + _subGroup.getName());
                    Logs.exception(e);
                }
            } else {
                try {
                    ExcelWriter.writeSubGroup(_subGroup);
                } catch (IOException | NoClassDefFoundError e) {
                    Logs.warning("Unable to write excel subGroup file : " + _subGroup.getName());
                    Logs.exception(e);
                }
                _subGroup.save();
            }
        });
        _currentSubGroup.start();
        return _currentSubGroup;
    }

}
