package Console;

import Utils.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

    /**
     * Keyword to start to activity
     */
    private static final String s_START = "start";
    /**
     * Keyword to stop to activity
     */
    private static final String s_STOP = "stop";
    /**
     * Keyword to pause to activity
     */
    private static final String s_PAUSE = "pause";
    /**
     * Keyword to resume to activity
     */
    private static final String s_RESUME = "resume";
    /**
     * Keyword to exit to activity
     */
    private static final String s_EXIT = "exit";

    /**
     * The console itself
     */
    private static Console s_console = null;

    /**
     * Action to launch
     */
    private Console.ActivityListener m_start = null;
    /**
     * Action to launch
     */
    private Console.ActivityListener m_stop = null;
    /**
     * Action to launch
     */
    private Console.ActivityListener m_pause = null;
    /**
     * Action to launch
     */
    private Console.ActivityListener m_resume = null;

    /**
     * Get the singleton
     *
     * @return the singleton
     */
    public static Console getSingleton() {
        if (s_console == null) {
            s_console = new Console();
        }
        return s_console;
    }

    /**
     * Add start action
     *
     * @param _activityListener the start action
     */
    public void addStartListener(Console.ActivityListener _activityListener) {
        m_start = _activityListener;
    }

    /**
     * Add stop action
     *
     * @param _activityListener the stop action
     */
    public void addStopListener(Console.ActivityListener _activityListener) {
        m_stop = _activityListener;
    }

    /**
     * Add pause action
     *
     * @param _activityListener the pause action
     */
    public void addPauseListener(Console.ActivityListener _activityListener) {
        m_pause = _activityListener;
    }

    /**
     * Add resume action
     *
     * @param _activityListener the resume action
     */
    public void addResumeListener(Console.ActivityListener _activityListener) {
        m_resume = _activityListener;
    }

    public void run() {
        try {
            String command = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (command.compareTo(s_EXIT) != 0) {
                command = br.readLine();
                if (command.compareTo(s_START) == 0) {
                    m_start.activityEvent();
                } else if (command.compareTo(s_STOP) == 0) {
                    m_stop.activityEvent();
                } else if (command.compareTo(s_PAUSE) == 0) {
                    m_pause.activityEvent();
                } else if (command.compareTo(s_RESUME) == 0) {
                    m_resume.activityEvent();
                }
            }
        } catch (IOException e) {
            Logs.exception(e);
        }
    }

    /**
     * Use to set set action of each button
     */
    public interface ActivityListener {
        boolean activityEvent();
    }

}
