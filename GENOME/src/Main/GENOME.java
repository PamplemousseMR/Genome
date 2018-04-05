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
        Logs.setListener(_message -> MainFrame.getSingleton().writeLog(_message));
        MainFrame.getSingleton().addDownloadAction(event -> {
            try {
                Activity.genbank(_message -> MainFrame.getSingleton().updateTree(_message));
            } catch (Exception e) {
                Logs.exception(e);
            }
        });

        initializeProgram();
        Runtime.getRuntime().addShutdownHook(new Thread(GENOME::finalizeProgram));
    }

}
