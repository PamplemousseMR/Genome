package Main;

import Data.IDataBase;
import GUI.MainFrame;
import Utils.Logs;
import Utils.Options;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

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
        unlock();
        Logs.finalizeLog();
    }

    private static boolean lock() {
        File mutex = new File(Options.getMutexFileName());
        try {
            return mutex.createNewFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Problèmme de création du verrou du programme.");
        }
        return false;
    }

    private static void unlock() {
        File mutex = new File(Options.getMutexFileName());
        if (mutex.exists()) {
            if (!mutex.delete()) {
                Logs.warning("Enable to delete mutex file : " + Options.getMutexFileName());
            }
        }
    }

    public static void main(String[] args) {
        if (lock()) {
            Logs.setListener((_message, _type) -> MainFrame.getSingleton().updateLog(_message, _type));
            MainFrame.getSingleton().addStartListener(Activity::genbank);
            MainFrame.getSingleton().addStopListener(Activity::stop);
            MainFrame.getSingleton().addPauseListener(Activity::pause);
            MainFrame.getSingleton().addResumeListener(Activity::resume);
            MainFrame.getSingleton().addTreeListener(_info -> {
                IDataBase organism = IDataBase.load(_info);
                if (organism != null) {
                    MainFrame.getSingleton().updateInformation(JDataBase.createComponent(organism));
                } else {
                    MainFrame.getSingleton().updateInformation(new JTextArea());
                }
            });
            initializeProgram();
            Runtime.getRuntime().addShutdownHook(new Thread(GENOME::finalizeProgram));
        } else {
            JOptionPane.showMessageDialog(null, "Programme en cours d'éxécution.");
        }
    }

}
