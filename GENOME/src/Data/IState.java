package Data;

public class IState extends IDataBase{

    public enum State{
        CREATED,
        STARTED,
        STOPPED,
        FINISHED
    }

    private State m_state;

    protected IState(String _name){
        super(_name);
        m_state = State.CREATED;
    }

    public void start() throws Exception{
        if(m_state == State.STARTED)
            throw new Exception("Already started");
        if(m_state == State.STOPPED || m_state == State.FINISHED)
            throw new Exception("Can't restart");
        m_state = State.STARTED;
    }

    public void stop() throws Exception{
        if(m_state == State.CREATED)
            throw new Exception("Not started");
        if(m_state == State.STOPPED)
            throw new Exception("Already stopped");
        if(m_state == State.FINISHED)
            throw new Exception("Already finished");
        m_state = State.STOPPED;
    }

    public State getState(){
        return m_state;
    }

    protected boolean finish() throws Exception{
        if(m_state == State.CREATED || m_state == State.STARTED)
            throw new Exception("Not stopped");
        if(m_state == State.FINISHED)
            throw new Exception("Already finished");
        m_state = State.FINISHED;
        return true;
    }

}
