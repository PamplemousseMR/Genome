package Data;

import Exception.InvalidStateException;
import Utils.Logs;
import Utils.Options;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

public class IDataBase implements Serializable {

    private static final long serialVersionUID = 2L;

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
     * Last modification's date
     */
    private transient final Date m_modificationDate;
    /**
     * Is the Data loaded or not
     */
    private transient final Boolean m_loaded;
    /**
     * The number of CDS sequences
     */
    private long m_CDSNumber;
    /**
     * The number of valid CDS sequences
     */
    private long m_validCDSNumber;
    /**
     * The number of underlying organism
     */
    private long m_totalOrganism;
    /**
     * Actual State
     */
    private transient State m_state;
    /**
     * Local index
     */
    private transient int m_index;
    /**
     * Total of finished children
     */
    private transient int m_finished;

    /**
     * Class constructor
     *
     * @param _name the name
     */
    protected IDataBase(String _name) {
        m_name = _name;
        m_modificationDate = new Date();
        m_statistics = new EnumMap<>(Statistics.Type.class);
        m_genomeNumber = new EnumMap<>(Statistics.Type.class);
        m_CDSNumber = 0L;
        m_validCDSNumber = 0L;
        m_totalOrganism = 0L;
        m_state = State.CREATED;
        m_index = -1;
        m_finished = 0;
        m_loaded = false;
    }

    /**
     * C
     *
     * @param _name the name
     * @param _data previous data
     */
    IDataBase(String _name, IDataBase _data) {
        m_name = _name;
        m_modificationDate = new Date();
        m_statistics = _data.m_statistics;
        m_genomeNumber = _data.m_genomeNumber;
        m_CDSNumber = _data.m_CDSNumber;
        m_validCDSNumber = _data.m_validCDSNumber;
        m_totalOrganism = _data.m_totalOrganism;
        m_state = State.CREATED;
        m_index = -1;
        m_finished = 0;
        m_loaded = true;
    }

    /**
     * Load a data from a file
     *
     * @param _name the name of the file to load
     * @return the IDatabase loaded
     */
    public static IDataBase load(String _name) {
        final File file = new File(Options.getSerializeDirectory() + File.separator + _name + Options.getSerializeExtension());
        ObjectInputStream stream = null;
        IDataBase result = null;
        if (!file.exists()) {
            return null;
        }
        try {
            stream = new ObjectInputStream((new FileInputStream(file)));
            result = (IDataBase) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logs.warning("Unable to load : " + _name);
            Logs.exception(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Logs.warning("Unable to close : " + _name);
                    Logs.exception(e);
                }
            }
        }
        return result;
    }

    /**
     * Start
     *
     * @throws InvalidStateException if it can't be started
     */
    public void start() throws InvalidStateException {
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
    public synchronized void stop() throws InvalidStateException {
        if (m_state == State.CREATED)
            throw new InvalidStateException("Not started : " + this.getName());
        if (m_state == State.STOPPED)
            throw new InvalidStateException("Already stopped : " + this.getName());
        if (m_state == State.FINISHED)
            throw new InvalidStateException("Already finished : " + this.getName());
        m_state = State.STOPPED;
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

    /**
     * Get the number of invalid sequences
     *
     * @return the number of invalid sequences
     */
    public final long getCDSNumber() {
        return m_CDSNumber;
    }

    /**
     * Get the number of valid sequences
     *
     * @return the number of valid sequences
     */
    public final long getValidCDSNumber() {
        return m_validCDSNumber;
    }

    /**
     * Get the number of underlying organism
     *
     * @return the number of underlying organism
     */
    public final long getTotalOrganism() {
        return m_totalOrganism;
    }

    /**
     * Save this data
     */
    public void save() {
        final File file = new File(Options.getSerializeDirectory() + File.separator + getSavedName() + Options.getSerializeExtension());
        ObjectOutputStream stream = null;
        if (file.exists()) {
            if (!file.delete()) {
                Logs.warning("Enable to delete file : " + file.getName());
            }
        }
        try {
            if (!file.createNewFile()) {
                Logs.warning("Enable to create file : " + file.getName());
            }
            stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(this);
            stream.flush();
        } catch (IOException e) {
            Logs.warning("Unable to save : " + getSavedName());
            Logs.exception(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Logs.warning("Unable to close : " + getSavedName());
                    Logs.exception(e);
                }
            }
        }
    }

    /**
     * Finish
     *
     * @throws InvalidStateException if it can't be finished
     */
    protected synchronized void finish() throws InvalidStateException {
        if (m_state == State.CREATED || m_state == State.STARTED)
            throw new InvalidStateException("Not stopped : " + this.getName());
        if (m_state == State.FINISHED)
            throw new InvalidStateException("Already finished : " + this.getName());
        m_state = State.FINISHED;
    }

    /**
     * Get the number of a genome's specified type
     *
     * @param _type, the Type of the genomes's number to get
     * @return the number of genomes
     */
    final long getTypeNumber(Statistics.Type _type) {
        return m_genomeNumber.get(_type);
    }

    /**
     * Set the local index
     *
     * @param _id, the index to set
     */
    final void setIndex(int _id) {
        m_index = _id;
    }

    /**
     * Increment by 1 the number of genome to a type
     *
     * @param _type, the Type of the genomes to increment
     */
    final void incrementGenomeNumber(Statistics.Type _type) {
        m_genomeNumber.merge(_type, 1L, (v1, v2) -> v1 + v2);
    }

    /**
     * Increment the number of genome of a type by the parameter
     *
     * @param _type, the Type of the genomes to increment
     * @param _inc,  the value of the increment
     */
    final void incrementGenomeNumber(Statistics.Type _type, long _inc) {
        m_genomeNumber.merge(_type, _inc, (v1, v2) -> v1 + v2);
    }

    /**
     * Create statistic if it's not exist and update it
     *
     * @param _statistics, the statistic to used for update
     */
    final void updateStatistics(Statistics _statistics) {
        m_statistics.computeIfAbsent(_statistics.getType(), k -> new Statistics(_statistics.getType()));
        m_statistics.get(_statistics.getType()).update(_statistics);
    }

    /**
     * Compute statistics
     */
    final void computeStatistics() {
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
    final <E> boolean contains(ArrayList<E> _arr, IDataBase _stat) {
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
    final void incrementFinishedChildren() {
        ++m_finished;
    }

    /**
     * Get the number of finish children
     *
     * @return the number of children finished
     */
    final int getFinishedChildren() {
        return m_finished;
    }

    /**
     * Clear data
     */
    final void clear() {
        m_statistics.clear();
        m_genomeNumber.clear();
    }

    /**
     * Increment the generic totals with those of another
     *
     * @param _data, the data used to increment
     */
    final void incrementGenericTotals(IDataBase _data) {
        m_CDSNumber += _data.m_CDSNumber;
        m_validCDSNumber += _data.m_validCDSNumber;
        m_totalOrganism += _data.m_totalOrganism;
    }

    /**
     * Increment the generic totals with those of a Statistics
     *
     * @param _stat, the data used to increment
     */
    final void incrementGenericTotals(Statistics _stat) {
        m_CDSNumber += _stat.getCDSNumber();
        m_validCDSNumber += _stat.getValidCDSNumber();
    }

    /**
     * Set the total of underlying organism to one
     * used for initialise Organism
     */
    final void setTotalOrganismToOne() {
        m_totalOrganism = 1L;
    }

    /**
     * Get the main part of the save path_name
     *
     * @return the main part of the save path_name
     */
    String getSavedName() {
        return getName();
    }

    /**
     * Unload data
     *
     * @param _data the data to unload
     */
    synchronized void unload(IDataBase _data) throws InvalidStateException {
        if (!m_loaded)
            throw new InvalidStateException("Not loaded : " + m_name + ". Requested by : " + _data.getName());

        m_CDSNumber -= _data.m_CDSNumber;
        m_validCDSNumber -= _data.m_validCDSNumber;
        m_totalOrganism -= _data.m_totalOrganism;

        for (Statistics stat : _data.m_statistics.values()) {
            Statistics.Type type = stat.getType();
            m_statistics.get(type).unload(stat);
            m_genomeNumber.put(type, m_genomeNumber.get(type) - _data.m_genomeNumber.get(type));
        }
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

