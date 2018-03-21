package Manager;

public abstract class ITask implements Runnable {

    /**
     * ITask's name
     */
    private final String m_name;

    /**
     * Class constructor
     *
     * @param _name, the name
     */
    public ITask(String _name) {
        m_name = _name;
    }

    /**
     * Get the name of this ITask
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Task to execute
     */
    @Override
    public abstract void run();
}
