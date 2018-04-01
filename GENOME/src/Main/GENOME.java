package Main;

import GUI.MainFrame;
import Utils.Logs;
import Utils.Options;

final class GENOME {

    /**
     * Function call at the begin of the program
     */
    private static void initializeProgram() {
        Logs.initializeLog();
        Logs.info("Log initialized");
        Options.initializeOptions();
        Logs.info("Options initialized");
        Logs.info("Begin");
    }

    /**
     * Function call at the end of the program
     */
    private static void finalizeProgram() {
        Logs.info("End");
        Logs.info("Options finalized");
        Options.finalizeOptions();
        Logs.info("Log finalized");
        Logs.finalizeLog();
    }

    public static void main(String[] args) {
        Logs.setListener(System.out::println);
        MainFrame.getSingleton().addDownloadAction(a -> (new Thread(() -> {
            try {
                Activity.genbank();
            } catch (Exception e) {
                Logs.exception(e);
            }
        })).start());

        initializeProgram();
        Runtime.getRuntime().addShutdownHook(new Thread(GENOME::finalizeProgram));
    }

}
