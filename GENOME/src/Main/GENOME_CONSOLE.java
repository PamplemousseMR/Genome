package Main;

import Console.Console;
import Utils.Logs;
import Utils.Options;

import java.io.File;
import java.io.IOException;

final class GENOME_CONSOLE {

    /**
     * Main
     *
     * @param args args
     */
    public static void main(String[] args) {
        if (lock()) {
            try {
                Logs.setListener((_message, _type) -> {
                    switch (_type) {
                        case EXCEPTION:
                            System.err.println(_message);
                            break;
                        default:
                            System.out.println(_message);
                            break;
                    }
                });
                initializeProgram();
                Runtime.getRuntime().addShutdownHook(new Thread(GENOME_CONSOLE::finalizeProgram));
                Console.getSingleton().addStartListener(GenomeConsoleActivity::genbank);
                Console.getSingleton().addStopListener(GenomeConsoleActivity::stop);
                Console.getSingleton().addPauseListener(GenomeConsoleActivity::pause);
                Console.getSingleton().addResumeListener(GenomeConsoleActivity::resume);
                Console.getSingleton().run();
            } catch (Throwable e) {
                Logs.exception(e);
            }
        }
    }

    /**
     * Function call at the begin of the program
     */
    private static void initializeProgram() {
        Logs.initializeLog();
        Logs.info("Log initialized", true);
        Options.initializeOptions();
        Logs.info("Options initialized", true);
        Logs.info("Begin", true);
    }

    /**
     * Function call at the end of the program
     */
    private static void finalizeProgram() {
        GenomeConsoleActivity.stopAndWait();
        Logs.info("End", true);
        Logs.info("Options finalized", true);
        Options.finalizeOptions();
        Logs.info("Log finalized", true);
        unlock();
        Logs.finalizeLog();
    }

    /**
     * Lock the programme
     *
     * @return true is the programme is lock
     */
    private static boolean lock() {
        File mutex = new File(Options.getMutexFileName());
        try {
            return mutex.createNewFile();
        } catch (IOException | SecurityException e) {
            Logs.warning("Enable to create mutex file : " + Options.getMutexFileName());
        }
        return false;
    }

    /**
     * Unlock the programme
     */
    private static void unlock() {
        File mutex = new File(Options.getMutexFileName());
        if (mutex.exists()) {
            try {
                if (!mutex.delete()) {
                    Logs.warning("Enable to delete mutex file : " + Options.getMutexFileName());
                }
            } catch (SecurityException e) {
                Logs.warning("Enable to delete mutex file : " + Options.getMutexFileName());
                Logs.exception(e);
            }
        }
    }

}
