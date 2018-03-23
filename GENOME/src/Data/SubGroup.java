package Data;

import Exception.AddException;
import Exception.InvalidStateException;

import java.util.ArrayList;

public final class SubGroup extends IDataBase {

    /**
     * Array of this SubGroup's Organisms
     */
    private transient final ArrayList<Organism> m_organisms;
    /**
     * Event to call when compute are finished
     */
    private transient final ISubGroupCallback m_event;
    /**
     * Reference to the parent
     */
    private transient Group m_parent;

    /**
     * Class constructor
     *
     * @param _name, the name of this SubGroup
     * @param _event the event call when compute is finished
     */
    public SubGroup(String _name, ISubGroupCallback _event) {
        super(_name);
        m_organisms = new ArrayList<>();
        m_parent = null;
        m_event = _event;
    }

    /**
     * Class constructor when already exist
     *
     * @param _name, the name of this SubGroup
     * @param _data, the previous version of this SubGroup
     * @param _event the event call when compute is finished
     */
    private SubGroup(String _name, IDataBase _data, ISubGroupCallback _event) {
        super(_name, _data);
        m_organisms = new ArrayList<>();
        m_parent = null;
        m_event = _event;
    }

    /**
     * Load a Subgroup with his name and affect the event
     * It create it if the file doesn't exist
     *
     * @param _name the name of the file to load
     * @param _parent the parent Group (used to know the path_name)
     * @param _event the Callback you want to apply
     * @return the Subgroup loaded or created
     */
    public static SubGroup load(String _name, Group _parent, ISubGroupCallback _event) {
        IDataBase result = IDataBase.load(_parent.getSavedName() + "__SG_" + _name);

        if (result == null) {
            return new SubGroup(_name, _event);
        } else {
            return new SubGroup(_name, result, _event);
        }
    }

    /**
     * Get the main part of the save path_name
     *
     * @return the main part of the save path_name
     */
    @Override
    protected String getSavedName() {
        return m_parent.getSavedName() + "__SG_" + getName();
    }

    /**
     * Add an Organism to this SubGroup
     *
     * @param _organism, the Organism to insert
     * @return the insertion success
     * @throws AddException if _organism are already added
     */
    public boolean addOrganism(Organism _organism) throws AddException {
        if (super.getState() == State.STARTED) {
            if (super.contains(m_organisms, _organism))
                throw new AddException("Organism already added : " + _organism.getName());
            _organism.setIndex(m_organisms.size());
            _organism.setParent(this);
            return m_organisms.add(_organism);
        } else return false;
    }

    /**
     * In case of all Organism are already finished
     *
     * @throws InvalidStateException if it can't be sopped
     */
    @Override
    public synchronized void stop() throws InvalidStateException {
        super.stop();
        if (super.getFinishedChildren() == m_organisms.size()) {
            end();
        }
    }

    /**
     * Get the Organism of this SubGroup
     *
     * @return the m_groups
     */
    public ArrayList<Organism> getOrganisms() {
        return m_organisms;
    }

    /**
     * Get the Group's name
     *
     * @return the Group's name
     */
    public String getGroupName() {
        return m_parent.getName();
    }

    /**
     * Get the Kingdom's name
     *
     * @return the Kingdom's name
     */
    public String getKingdomName() {
        return m_parent.getParent().getName();
    }

    /**
     * Finish this Subgroup if it can
     *
     * @param _organism, the Organism to finish
     * @throws InvalidStateException if it can't be finished
     */
    protected synchronized void finish(Organism _organism) throws InvalidStateException {
        if (super.contains(m_organisms, _organism) && _organism.getState() != State.FINISHED) {
            for (Statistics stat : _organism.getStatistics().values()) {
                super.updateStatistics(stat);
                super.incrementGenomeNumber(stat.getType(), _organism.getTypeNumber(stat.getType()));
            }
            super.incrementGenericTotals(_organism);
            super.incrementFinishedChildren();
            if (super.getState() == State.STOPPED && super.getFinishedChildren() == m_organisms.size()) {
                end();
            }
        }
    }

    /**
     * Get the parent
     *
     * @return the parent
     */
    public Group getParent() {
        return m_parent;
    }

    /**
     * Set the parent
     *
     * @param _group, the parent to set
     */
    protected void setParent(Group _group) {
        m_parent = _group;
    }

    /**
     * Call callback and clear data
     *
     * @throws InvalidStateException if an exception appear
     */
    private void end() throws InvalidStateException {
        super.computeStatistics();
        m_event.finish(this);
        m_parent.finish(this);
        super.finish();
        m_organisms.clear();
        super.clear();
    }


    protected void unload(Organism _org) {
        if(super.getLoadState() == LoadState.LOAD){
            m_parent.unload(this);
            setLoadState(LoadState.UNLOAD);
        }
        super.unload(_org);
    }

    /**
     * Start
     *
     * @throws InvalidStateException if it can't be started
     */
    @Override
    public final void start() throws InvalidStateException {
        if(m_parent == null)
            throw new InvalidStateException("Unable to start without been add in a Group : " + getName());
        super.start();
    }
}
