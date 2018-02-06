package Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

public class IDataBase {

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
     * Local index
     */
    private int m_index;
    /**
     * Total of finished children
     */
    private int m_finished;
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
    private EnumMap<Statistics.Type,Statistics> m_statistics;
    /**
     * Array of values of each Replicon's type
     */
    private EnumMap<Statistics.Type,Long> m_genomeNumber;

    /**
     * Class constructor
     */
    protected IDataBase(String _name){
        m_name = _name;
        m_modificationDate = new Date();
        m_statistics = new EnumMap<>(Statistics.Type.class);
        m_genomeNumber = new EnumMap<>(Statistics.Type.class);
        m_state = State.CREATED;
        m_index = -1;
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

    /**
     * Get the statistics
     * @return the statistics
     */
    public EnumMap<Statistics.Type,Statistics> getStatistics() {
         return m_statistics;
    }

    /**
     * Get number of each Genome's Type
     * @return the number of each Genome's Type
     */
    public EnumMap<Statistics.Type, Long> getGenomeNumber() {
        return m_genomeNumber;
    }

    // Do not used

    /**
     * Get the number of a genome's specified type
     * @param _type, the Type of the genomes's number to get
     * @return the number of genomes
     */
    protected Long getTypeNumber(Statistics.Type _type) {
        return m_genomeNumber.get(_type);
    }

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

    /**
     * Set the local index
     * @param _id, the index to set
     */
    protected void setIndex(int _id){
        m_index = _id;
    }

    /**
     * Increment by 1 the number of genome to a type
     * @param _type, the Type of the genomes to increment
     */
    protected void incrementGenomeNumber(Statistics.Type _type) {
        m_genomeNumber.merge(_type, 1L, (v1,v2) -> v1 + v2);
    }

    /**
     * Increment by _incr the number of genome of a type
     * @param _type, the Type of the genomes to increment
     * @param _incr, the value of the increment
     */
    protected void incrementGenomeNumber(Statistics.Type _type,long _incr){
        m_genomeNumber.merge(_type, _incr, (v1,v2) -> v1 + v2);
    }

    /**
     * Create statistic if it's not exist and update it
     * @param _statistics, the statistic to used for update
     */
    protected void updateStatistics(Statistics _statistics){
        if(m_statistics.get(_statistics.getType()) == null){
            m_statistics.put(_statistics.getType(), new Statistics(_statistics.getType()));
        }
        m_statistics.get(_statistics.getType()).update(_statistics);
    }

    /**
     * Compute statistics
     */
    protected void computeStatistics(){
        m_statistics.values().parallelStream().forEach(Statistics::compute);
    }

    /**
     * Return true if the array contains the IState
     * @param _arr, the array to search
     * @param _stat, the IStat =e to find
     * @param <E>, the class of the array
     * @return the find success
     */
    protected <E> boolean contains(ArrayList<E> _arr, IDataBase _stat){
        try{
            if(_arr.get(_stat.m_index) != null) {
                return true;
            }
        }catch (IndexOutOfBoundsException e){
            return false;
        }
        return false;
    }

    /**
     * Increment the number of finish children
     */
    protected void incrementFinishedChildrens(){
        ++m_finished;
    }

    /**
     * Get the number of finish children
     * @return the number of children finished
     */
    protected int getFinishedChildrens(){
        return m_finished;
    }

}

