package Data;

import Exception.AddException;
import Exception.InvalidStateException;

import java.util.ArrayList;

public final class Organism extends IDataBase {

    /**
     * Array of this organism's Replicon
     */
    private final ArrayList<Replicon> m_replicons;
    /**
     * The id of this organism
     */
    private final long m_id;
    /**
     * The version of the organism
     */
    private final long m_version;
    /**
     * Reference to the parent
     */
    private SubGroup m_parent;

    /**
     * Class constructor
     *
     * @param _name    the name of the organism
     * @param _id      the id of the organism
     * @param _version the version of the organism
     */
    public Organism(String _name, long _id, long _version) {
        super(_name);
        m_id = _id;
        m_version = _version;
        m_replicons = new ArrayList<>();
        m_parent = null;
    }

    /**
     * Add a Replicon to this Organism
     *
     * @param _replicon, the Replicon to insert
     * @return the insertion success
     * @throws AddException if it _replicon are already added
     */
    public boolean addReplicon(Replicon _replicon) throws AddException {
        if (super.getState() == State.STARTED) {
            try {
                if (m_replicons.get(_replicon.getIndex()) != null)
                    throw new AddException("Replicon already added : " + _replicon.getName());
            } catch (IndexOutOfBoundsException e) {
            }
            _replicon.setIndex(m_replicons.size());
            return m_replicons.add(_replicon);
        } else return false;
    }

    /**
     * Update the statistics
     *
     * @throws InvalidStateException if it can't be finished
     */
    @Override
    public void finish() throws InvalidStateException {
        m_replicons.parallelStream().forEach(Replicon::computeStatistic);
        for (Replicon rep : m_replicons) {
            super.updateStatistics(rep);
            super.incrementGenomeNumber(rep.getType());
        }
        m_replicons.clear();
        super.computeStatistics();
        m_parent.finish(this);
        super.finish();
    }

    /**
     * Get the replicons Organism
     *
     * @return the m_replicons
     */
    public ArrayList<Replicon> getReplicons() {
        return m_replicons;
    }

    /**
     * Get the SubGroup's name
     *
     * @return the SubGroup's name
     */
    public String getSubGroupName() {
        return m_parent.getName();
    }

    /**
     * Get the Group's name
     *
     * @return the Group's name
     */
    public String getGroupName() {
        return m_parent.getParent().getName();
    }

    /**
     * Get the Kingdom's name
     *
     * @return the Kingdom's name
     */
    public String getKingdomName() {
        return m_parent.getParent().getParent().getName();
    }

    /**
     * Get the version of the organism
     *
     * @return the version's number
     */
    public long getVersion() {
        return m_version;
    }

    /**
     * Get the id of the organism
     *
     * @return the id's number
     */
    public long getId() {
        return m_id;
    }

    // Do not use

    /**
     * Set the parent
     *
     * @param _subGroup, the parent to set
     */
    protected void setParent(SubGroup _subGroup) {
        m_parent = _subGroup;
    }
}
