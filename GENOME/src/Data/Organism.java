package Data;

import Exception.AddException;
import Exception.InvalidStateException;

import java.util.ArrayList;

public final class Organism extends IDataBase {

    /**
     * Array of this organism's Replicon
     */
    private transient final ArrayList<Replicon> m_replicons;
    /**
     * The id of this organism
     */
    private transient final long m_id;
    /**
     * The version of the organism
     */
    private transient final long m_version;
    /**
     * Event to call when compute are finished
     */
    private transient final IOrganismCallback m_event;
    /**
     * Reference to the parent
     */
    private transient SubGroup m_parent;

    /**
     * Class constructor
     *
     * @param _name    the name of the organism
     * @param _id      the id of the organism
     * @param _version the version of the organism
     * @param _event   the event call when compute is finished
     */
    public Organism(String _name, long _id, long _version, IOrganismCallback _event) {
        super(_name);
        m_id = _id;
        m_version = _version;
        m_replicons = new ArrayList<>();
        m_parent = null;
        m_event = _event;
        super.setTotalOrganismToOne();
    }

    /**
     * Load a Organism with his name, his id and his version and affect the event
     * You can choose to create a newOne with unloadTheLas if it exist
     *
     * @param _name the name of the organism
     * @param _id the id of the organism
     * @param _version the version of the organism
     * @param _parent the parent SubGroup (used to know the path_name and to unload it)
     * @param _unloadLastCreateNew true for create a new one and unfold the last, false to get the last
     * @param _event the Callback you want to apply
     * @return the Organism loaded or created
     */
    public static Organism load(String _name, long _id, long _version, SubGroup _parent, Boolean _unloadLastCreateNew, IOrganismCallback _event) {
        Organism lastOne = (Organism) IDataBase.load(_parent.getSavedName() + "__O_" + _name);
        if(_unloadLastCreateNew){
            if(lastOne != null)
                _parent.unload(lastOne);
            return new Organism(_name, _id, _version, _event);
        }else{
            return lastOne;
        }
    }

    /**
     * Get the main part of the save path_name
     *
     * @return the main part of the save path_name
     */
    @Override
    protected String getSavedName() {
        return m_parent.getSavedName() + "__O_" + getName();
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
            super.incrementGenericTotals(rep);
        }
        super.computeStatistics();
        m_event.finish(this);
        m_parent.finish(this);
        super.finish();
        m_replicons.clear();
        super.clear();
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

    /**
     * Get the parent
     *
     * @return the parent
     */
    public SubGroup getParent() {
        return m_parent;
    }

    /**
     * Set the parent
     *
     * @param _subGroup, the parent to set
     */
    protected void setParent(SubGroup _subGroup) {
        m_parent = _subGroup;
    }


    /**
     * Start
     *
     * @throws InvalidStateException if it can't be started
     */
    @Override
    public final void start() throws InvalidStateException {
        if(m_parent == null)
            throw new InvalidStateException("Unable to start without been add in a SubGroup : " + getName());
        super.start();
    }
}
