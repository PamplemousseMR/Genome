package Data;

import java.util.Date;

public class IDataBase {

    /**
     * The name
     */
    private String m_name;
    /**
     * Last modification's date
     */
    private Date m_modificationDate;
    /**
     * Statistics of this IDataBase
     */
    private Statistics m_statistics;

    /**
     * Class constructor
     */
    protected IDataBase(String _name){
        m_name = _name;
        m_modificationDate = new Date();
        m_statistics = new Statistics();
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

    public Statistics getStatistics() {
        return m_statistics;
    }
}
