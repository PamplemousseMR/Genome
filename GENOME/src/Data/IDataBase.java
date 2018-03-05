package Data;

import Exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

public class IDataBase {

    /**
     * The name
     */
    private final String m_name;
    /**
     * Statistics of this IDataBase
     */
    private final EnumMap<Statistics.Type, Statistics> m_statistics;
    /**
     * Array of values of each Replicon's type
     */
    private final EnumMap<Statistics.Type, Long> m_genomeNumber;
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
     * Last modification's date
     */
    private Date m_modificationDate;

    /**
     * Class constructor
     */
    protected IDataBase(String _name) {
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
     *
     * @throws InvalidStateException if it can't be started
     */
    public final void start() throws InvalidStateException {
        if (m_state == State.STARTED)
            throw new InvalidStateException("Already started : " + this.getName());
        if (m_state == State.STOPPED || m_state == State.FINISHED)
            throw new InvalidStateException("Can't restart : " + this.getName());
        m_state = State.STARTED;
    }

    /**
     * Stop
     *
     * @throws InvalidStateException if it can't be stopped
     */
    public void stop() throws InvalidStateException {
        if (m_state == State.CREATED)
            throw new InvalidStateException("Not started : " + this.getName());
        if (m_state == State.STOPPED)
            throw new InvalidStateException("Already stopped : " + this.getName());
        if (m_state == State.FINISHED)
            throw new InvalidStateException("Already finished : " + this.getName());
        m_state = State.STOPPED;
    }

    /**
     * Finish
     *
     * @throws InvalidStateException if it can't be finished
     */
    protected void finish() throws InvalidStateException {
        if (m_state == State.CREATED || m_state == State.STARTED)
            throw new InvalidStateException("Not stopped : " + this.getName());
        if (m_state == State.FINISHED)
            throw new InvalidStateException("Already finished : " + this.getName());
        m_state = State.FINISHED;
    }

    /**
     * Get actual State
     *
     * @return the State
     */
    public final State getState() {
        return m_state;
    }

    /**
     * Get the last modification's date
     *
     * @return the m_modificationDate
     */
    public final Date getModificationDate() {
        return m_modificationDate;
    }

    /**
     * Get the name
     *
     * @return the m_name
     */
    public final String getName() {
        return m_name;
    }

    /**
     * Get the statistics
     *
     * @return the statistics
     */
    public final EnumMap<Statistics.Type, Statistics> getStatistics() {
        return m_statistics;
    }

    /**
     * Get number of each Genome's Type
     *
     * @return the number of each Genome's Type
     */
    public final EnumMap<Statistics.Type, Long> getGenomeNumber() {
        return m_genomeNumber;
    }

    // Do not used

    /**
     * Get the number of a genome's specified type
     *
     * @param _type, the Type of the genomes's number to get
     * @return the number of genomes
     */
    protected final Long getTypeNumber(Statistics.Type _type) {
        return m_genomeNumber.get(_type);
    }

    /**
     * Set the local index
     *
     * @param _id, the index to set
     */
    protected final void setIndex(int _id) {
        m_index = _id;
    }

    /**
     * Increment by 1 the number of genome to a type
     *
     * @param _type, the Type of the genomes to increment
     */
    protected final void incrementGenomeNumber(Statistics.Type _type) {
        m_genomeNumber.merge(_type, 1L, (v1, v2) -> v1 + v2);
    }

    /**
     * Increment the number of genome of a type by the parameter
     *
     * @param _type, the Type of the genomes to increment
     * @param _inc,  the value of the increment
     */
    protected final void incrementGenomeNumber(Statistics.Type _type, long _inc) {
        m_genomeNumber.merge(_type, _inc, (v1, v2) -> v1 + v2);
    }

    /**
     * Create statistic if it's not exist and update it
     *
     * @param _statistics, the statistic to used for update
     */
    protected final void updateStatistics(Statistics _statistics) {
        m_statistics.computeIfAbsent(_statistics.getType(), k -> new Statistics(_statistics.getType()));
        m_statistics.get(_statistics.getType()).update(_statistics);
    }

    /**
     * Compute statistics
     */
    protected final void computeStatistics() {
        m_statistics.values().parallelStream().forEach(Statistics::compute);
    }

    /**
     * Return true if the array contains the IState
     *
     * @param _arr,  the array to search
     * @param _stat, the IStat =e to find
     * @param <E>,   the class of the array
     * @return the find success
     */
    protected final <E> boolean contains(ArrayList<E> _arr, IDataBase _stat) {
        try {
            if (_arr.get(_stat.m_index) == null) {
                return false;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * Increment the number of finish children
     */
    protected final void incrementFinishedChildren() {
        ++m_finished;
    }

    /**
     * Get the number of finish children
     *
     * @return the number of children finished
     */
    protected final int getFinishedChildren() {
        return m_finished;
    }

    /**
     * Type of each State
     */
    public enum State {
        CREATED,
        STARTED,
        STOPPED,
        FINISHED
    }

}

