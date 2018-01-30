package Data;

import java.util.Date;

public class IDataBase {

    public enum State{
        WIP,
        DONE
    }
    /**
     * The name
     */
    private String m_name;
    /**
     * Last modification's date
     */
    private Date m_modificationDate;
    /**
     * Actual state of this IDataBase
     */
    private State m_state;
    /**
     * Statistics of this IDataBase
     */
    protected Statistics m_statistics;

    /**
     * Class constructor
     */
    protected IDataBase(String _name){
        m_name = _name;
        m_modificationDate = new Date();
        m_state = State.WIP;
        m_statistics = new Statistics();
    }

    /**
     * Specified to this IDataBase that all this children are created
     */
    public void finish(){
        m_state = State.DONE;
    }

    /**
     * Get the state
     * @return the state
     */
    public State getState() {
        return m_state;
    }

    /**
     * Get the last modification's date
     * @return the m_modificationDate
     */
    public Date getModificationDate() {
        return m_modificationDate;
    }

    /**
     * Get the name
     * @return the m_name
     */
    public String getName(){
        return m_name;
    }

}
