package Manager;

public abstract class IDownload extends ITask {

    /**
     * Class constructor
     *
     * @param _name, the name
     */
    protected IDownload(String _name) {
        super(_name, TaskType.DOWNLOAD);
    }

    /**
     * Task to execute
     */
    @Override
    public abstract void run();

}
