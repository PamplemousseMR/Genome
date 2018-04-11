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
        Logs.info("Log initialized", true);
        Options.initializeOptions();
        Logs.info("Options initialized", true);
        Logs.info("Begin", true);
    }

    /**
     * Function call at the end of the program
     */
    private static void finalizeProgram() {
        Activity.stopAndWait();
        Logs.info("End", true);
        Logs.info("Options finalized", true);
        Options.finalizeOptions();
        Logs.info("Log finalized", true);
        Logs.finalizeLog();
    }

    public static void main(String[] args) {
        Logs.setListener((_message, _type) -> MainFrame.getSingleton().updateLog(_message, _type));
        MainFrame.getSingleton().setStartAction(() -> Activity.genbank());
        MainFrame.getSingleton().setStopAction(() -> Activity.stop());
        MainFrame.getSingleton().setPauseAction(() -> Activity.pause());
        MainFrame.getSingleton().setResumeAction(() -> Activity.resume());
        initializeProgram();
        Runtime.getRuntime().addShutdownHook(new Thread(GENOME::finalizeProgram));
    }

}
