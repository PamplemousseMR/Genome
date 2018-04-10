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
        Activity.stopAndWait();
        Logs.info("End");
        Logs.info("Options finalized");
        Options.finalizeOptions();
        Logs.info("Log finalized");
        Logs.finalizeLog();
    }

    public static void main(String[] args) {
        Logs.setListener(_message -> MainFrame.getSingleton().updateLog(_message));
        MainFrame.getSingleton().setStartAction(event -> Activity.genbank());
        MainFrame.getSingleton().setStopAction(event -> Activity.stop());
        MainFrame.getSingleton().setPauseAction(event -> Activity.pause());
        MainFrame.getSingleton().setResumeAction(event -> Activity.resume());
        initializeProgram();
        Runtime.getRuntime().addShutdownHook(new Thread(GENOME::finalizeProgram));
    }

}
