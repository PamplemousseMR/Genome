package Data;

import Exception.AddException;
import Exception.InvalidStateException;

import java.util.ArrayList;

public final class Group extends IDataBase {

    /**
     * Prefix used for serialization
     */
    private static final String s_SERIALIZATION_PREFIX = "__G_";
    /**
     * Array of this Group's SubGroups
     */
    private transient final ArrayList<SubGroup> m_subGroups;
    /**
     * Event to call when compute are finished
     */
    private transient final IGroupCallback m_event;
    /**
     * Reference to the parent
     */
    private transient Kingdom m_parent;

    /**
     * Class constructor
     *
     * @param _name, the name of this Group
     * @param _event the event call when compute is finished
     */
    private Group(String _name, IGroupCallback _event) {
        super(_name);
        m_subGroups = new ArrayList<>();
        m_parent = null;
        m_event = _event;
    }

    /**
     * Class constructor when already exist
     *
     * @param _name, the name of this Group
     * @param _data, the previous version of this Group
     * @param _event the event call when compute is finished
     */
    private Group(String _name, IDataBase _data, IGroupCallback _event) {
        super(_name, _data);
        m_subGroups = new ArrayList<>();
        m_parent = null;
        m_event = _event;
    }

    /**
     * Load a Group with his name and affect the event
     * It create it if the file doesn't exist
     *
     * @param _name   the name of the file to load
     * @param _parent the parent Kingdom (used to know the path_name)
     * @param _event  the Callback you want to apply
     * @return the Group loaded or created
     */
    public static Group load(String _name, Kingdom _parent, IGroupCallback _event) throws AddException, InvalidStateException {
        final IDataBase lastOne = IDataBase.load(_parent.getSavedName() + s_SERIALIZATION_PREFIX + _name);
        final Group newOne;
        if (lastOne == null) {
            newOne = new Group(_name, _event);
        } else {
            newOne = new Group(_name, lastOne, _event);
            _parent.unload(newOne);
        }
        _parent.addGroup(newOne);
        return newOne;
    }

    /**
     * In case of all SubGroup are already finished
     *
     * @throws InvalidStateException if it can't be stopped
     */
    @Override
    public synchronized void stop() throws InvalidStateException {
        super.stop();
        if (getFinishedChildren() == m_subGroups.size()) {
            end();
        }
    }

    /**
     * Get the Subgroups of this Group
     *
     * @return the m_subGroups
     */
    public ArrayList<SubGroup> getSubGroups() {
        return m_subGroups;
    }

    /**
     * Get the Kingdom's name
     *
     * @return the Kingdom's name
     */
    public String getKingdomName() {
        return m_parent.getName();
    }

    /**
     * Set the parent
     *
     * @param _kingdom, the parent to set
     */
    protected void setParent(Kingdom _kingdom) {
        m_parent = _kingdom;
    }

    /**
     * Start
     *
     * @throws InvalidStateException if it can't be started
     */
    @Override
    public final void start() throws InvalidStateException {
        if (m_parent == null)
            throw new InvalidStateException("Unable to start without been add in a Kingdom : " + getName());
        super.start();
    }

    /**
     * Finish this Group if it can
     *
     * @param _subGroup, the SubGroup to finish
     * @throws InvalidStateException if it can't be finished
     */
    protected synchronized void finish(SubGroup _subGroup) throws InvalidStateException {
        if (super.contains(m_subGroups, _subGroup) && _subGroup.getState() != State.FINISHED) {
            for (Statistics stat : _subGroup.getStatistics().values()) {
                super.updateStatistics(stat);
                super.incrementGenomeNumber(stat.getType(), _subGroup.getTypeNumber(stat.getType()));
            }
            super.incrementGenericTotals(_subGroup);
            super.incrementFinishedChildren();
            if (super.getState() == State.STOPPED && super.getFinishedChildren() == m_subGroups.size()) {
                end();
            }
        }
    }

    /**
     * Get the main part of the save file name
     *
     * @return the main part of the save path_name
     */
    @Override
    protected String getSavedName() {
        return m_parent.getSavedName() + s_SERIALIZATION_PREFIX + getName();
    }

    /**
     * Add a SubGroup to this Group
     *
     * @param _subGroup, the Subgroup to insert
     * @return the insertion success
     * @throws AddException if _subGroup are already added
     */
    protected boolean addSubGroup(SubGroup _subGroup) throws AddException {
        if (super.getState() == State.STARTED) {
            if (super.contains(m_subGroups, _subGroup))
                throw new AddException("SubGroup already added : " + _subGroup.getName());
            _subGroup.setIndex(m_subGroups.size());
            _subGroup.setParent(this);
            return m_subGroups.add(_subGroup);
        } else return false;
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
        m_subGroups.clear();
        super.clear();
    }
}
