package Data;

import Exception.AddException;
import Exception.InvalidStateException;

import java.util.ArrayList;

public final class DataBase extends IDataBase {

    /**
     * Instance of the singleton
     */
    private static DataBase s_DataBase;
    /**
     * Array of this Database's Kingdom
     */
    private final ArrayList<Kingdom> m_kingdoms;
    /**
     * Event to call when compute are finished
     */
    private final IDataBaseCallback m_event;

    /**
     * Class constructor
     *
     * @param _name, the name of this DataBase
     * @param _event the event call when compute is finished
     */
    public DataBase(String _name, IDataBaseCallback _event) {
        super(_name);
        m_kingdoms = new ArrayList<>();
        m_event = _event;
    }

    /**
     * Add a Kingdom to the Database
     *
     * @param _kingdom, the Kingdom to insert
     * @return the insertion success
     * @throws AddException if _kingdom are already added
     */
    public boolean addKingdom(Kingdom _kingdom) throws AddException {
        if (super.getState() == State.STARTED) {
            if (super.contains(m_kingdoms, _kingdom))
                throw new AddException("Kingdom already added : " + _kingdom.getName());
            _kingdom.setIndex(m_kingdoms.size());
            _kingdom.setParent(this);
            return m_kingdoms.add(_kingdom);
        } else return false;
    }

    /**
     * Get the Kingdoms of this DataBase
     *
     * @return the m_kingdoms
     */
    public ArrayList<Kingdom> getKingdoms() {
        return m_kingdoms;
    }

    /**
     * In case of all Kingdom are already finished
     *
     * @throws InvalidStateException if it can't be stopped
     */
    @Override
    public void stop() throws InvalidStateException {
        super.stop();
        if (super.getFinishedChildren() == m_kingdoms.size()) {
            end();
        }
    }

    // Do not use

    /**
     * Finish this DataBase if it can
     *
     * @param _kingdom, the Kingdom to finish
     * @throws InvalidStateException if it can't be finished
     */
    protected void finish(Kingdom _kingdom) throws InvalidStateException {
        if (super.contains(m_kingdoms, _kingdom) && _kingdom.getState() != State.FINISHED) {
            for (Statistics stat : _kingdom.getStatistics().values()) {
                super.updateStatistics(stat);
                super.incrementGenomeNumber(stat.getType(), _kingdom.getTypeNumber(stat.getType()));
            }
            super.incrementGenericTotals(_kingdom);
            incrementFinishedChildren();
            if (super.getState() == IDataBase.State.STOPPED && super.getFinishedChildren() == m_kingdoms.size()) {
                end();
            }
        }
    }

    /**
     * Call callback and clear data
     *
     * @throws InvalidStateException if an exception appear
     */
    private void end() throws InvalidStateException {
        super.computeStatistics();
        m_event.finish(this);
        super.finish();
        m_kingdoms.clear();
        super.clear();
    }

}
