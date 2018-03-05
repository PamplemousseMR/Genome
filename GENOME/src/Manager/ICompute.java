package Manager;

public abstract class ICompute extends ITask {

    /**
     * Class constructor
     *
     * @param _name, the name
     */
    protected ICompute(String _name) {
        super(_name, TaskType.COMPUTING);
    }

    /**
     * Task to execute
     */
    @Override
    public abstract void run();

}
