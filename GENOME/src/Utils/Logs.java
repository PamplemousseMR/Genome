package Utils;

import java.io.*;

public final class Logs {

    /**
     * File's name
     */
    private static final String s_LOGS_FILE_NAME = "log.txt";
    /**
     * Buffer to write in file
     */
    private static BufferedWriter s_file = null;
    /**
     * Logs listener
     */
    private static LogsListener s_logsListener;

    /**
     * Open the log file in order to write in it
     */
    public static void initializeLog() {
        if (s_file == null) {
            File file = new File(s_LOGS_FILE_NAME);
            try {
                s_file = new BufferedWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            s_logsListener = _message -> {
            };
        }
    }

    /**
     * Close the log file
     */
    public static void finalizeLog() {
        try {
            if (s_file != null)
                s_file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the listener
     *
     * @param _logsListener the listener to set
     */
    public static void setListener(LogsListener _logsListener) {
        s_logsListener = _logsListener;
    }

    /**
     * Print a message in the log file
     *
     * @param _message the message to print
     */
    public static void info(String _message) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final String header = "[" + stackTraceElements[2].getFileName() + "{ " + stackTraceElements[2].getClassName() + " : " + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ") } ] info : ";
        final String message = header + " " + _message;
        if (s_file != null) {
            try {
                s_file.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        s_logsListener.logsEvent(message);
    }

    /**
     * Print a message in the log file
     *
     * @param _message the message to print
     */
    public static void warning(String _message) {
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final String header = "[" + stackTraceElements[2].getFileName() + "{ " + stackTraceElements[2].getClassName() + " : " + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ") } ] warning : ";
        final String message = header + " " + _message;
        if (s_file != null) {
            try {
                s_file.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        s_logsListener.logsEvent(message);
    }

    /**
     * Print a exception in the log file
     *
     * @param _exception the message to print
     */
    public static void exception(Exception _exception) {
        StringWriter errors = new StringWriter();
        _exception.printStackTrace(new PrintWriter(errors));

        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        final String header = "[" + stackTraceElements[2].getFileName() + "{ " + stackTraceElements[2].getClassName() + " : " + stackTraceElements[2].getMethodName() + "(" + stackTraceElements[2].getLineNumber() + ") } ] Exception : ";
        final String message = header + " " + errors.toString();
        if (s_file != null) {
            try {
                s_file.write(message + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        s_logsListener.logsEvent(message);
    }

    public interface LogsListener {
        void logsEvent(String _message);
    }
}
