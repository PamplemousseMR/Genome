package Manager;

public abstract class ITask implements Runnable{

    /**
     * ITask's type list
     */
    protected enum TaskType{
        DOWNLOAD,
        COMPUTING
    }

    /**
     * ITask's name
     */
    private final String m_name;
    /**
     * ITask's type
     */
    private final TaskType m_type;

    /**
     * Class constructor
     * @param _name, the name
     * @param _type, the type
     */
    ITask(String _name, TaskType _type){
        m_name = _name;
        m_type = _type;
    }

    /**
     * Get the name of this ITask
     * @return the name
     */
    public String getName(){
        return m_name;
    }

    /**
     * Get the type of this ITask
     * @return the type
     */
    public TaskType getTaskType(){
        return m_type;
    }

    /**
     * Task to execute
     */
    @Override
    public abstract void run();

}
