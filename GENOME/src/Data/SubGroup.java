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
    public void stop() throws InvalidStateException {
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

    // Do not use

    /**
     * Finish this Subgroup if it can
     *
     * @param _organism, the Organism to finish
     * @throws InvalidStateException if it can't be finished
     */
    protected void finish(Organism _organism) throws InvalidStateException {
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
     * Get the Group
     *
     * @return the Group
     */
    protected Group getParent() {
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

}
