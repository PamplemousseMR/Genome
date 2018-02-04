package Data;

import java.util.ArrayList;

public class IState extends IDataBase{

    /**
     * Type of each State
     */
    public enum State{
        CREATED,
        STARTED,
        STOPPED,
        FINISHED
    }

    /**
     * Actual State
     */
    private State m_state;
    /**
     * ID
     */
    private int m_id;
    /**
     * Total of finished children
     */
    private int m_finished;

    /**
     * Class constructor
     * @param _name, the name
     */
    protected IState(String _name){
        super(_name);
        m_state = State.CREATED;
        m_id = -1;
        m_finished = 0;
    }

    /**
     * Start
     * @throws Exception if it can't be started
     */
    public void start() throws Exception{
        if(m_state == State.STARTED)
            throw new Exception("Already started");
        if(m_state == State.STOPPED || m_state == State.FINISHED)
            throw new Exception("Can't restart");
        m_state = State.STARTED;
    }

    /**
     * Stop
     * @throws Exception if it can't be stopped
     */
    public void stop() throws Exception{
        if(m_state == State.CREATED)
            throw new Exception("Not started");
        if(m_state == State.STOPPED)
            throw new Exception("Already stopped");
        if(m_state == State.FINISHED)
            throw new Exception("Already finished");
        m_state = State.STOPPED;
    }

    /**
     * Get actual State
     * @return the State
     */
    public State getState(){
        return m_state;
    }

    // Do not used

    /**
     * Finish
     * @throws Exception if it can't be finished
     */
    protected void finish() throws Exception{
        if(m_state == State.CREATED || m_state == State.STARTED)
            throw new Exception("Not stopped");
        if(m_state == State.FINISHED)
            throw new Exception("Already finished");
        m_state = State.FINISHED;
    }

    protected void setID(int _id){
        m_id = _id;
    }

    protected int getID(){
        return m_id;
    }

    protected <E> boolean contains(ArrayList<E> _arr, IState _stat){
        try{
            if(_arr.get(_stat.getID()) != null) {
                return true;
            }
        }catch (IndexOutOfBoundsException e){}
        return false;
    }

    protected void incrementFinishedChildrens(){
        ++m_finished;
    }

    protected int getFinishedChildrens(){
        return m_finished;
    }

}
