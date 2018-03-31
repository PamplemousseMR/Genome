package Data;

import Exception.AddException;
import Exception.InvalidStateException;
import Utils.Logs;
import Utils.Options;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public final class Organism extends IDataBase {

    /**
     * Prefix used for serialization
     */
    private static final String s_SERIALIZATION_PREFIX = "--O_";
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
    private Organism(String _name, long _id, long _version, IOrganismCallback _event) {
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
     * @param _name                the name of the organism
     * @param _id                  the id of the organism
     * @param _version             the version of the organism
     * @param _parent              the parent SubGroup (used to know the path_name and to unload it)
     * @param _unloadLastCreateNew true for create a new one and unfold the last, false to get the last
     * @param _event               the Callback you want to apply
     * @return the Organism loaded or created
     */
    public static Organism load(String _name, long _id, long _version, SubGroup _parent, Boolean _unloadLastCreateNew, IOrganismCallback _event) throws AddException, InvalidStateException {
        Organism lastOne = (Organism) IDataBase.load(_parent.getSavedName() + s_SERIALIZATION_PREFIX + _name);
        if (_unloadLastCreateNew) {
            if (lastOne != null)
                _parent.unload(lastOne);
            lastOne = new Organism(_name, _id, _version, _event);
            _parent.addOrganism(lastOne);
        }
        return lastOne;
    }

    public static Date loadDate(String _db, String _ki, String _gp, String _sg, String _name) {
        String fileName = DataBase.s_SERIALIZATION_PREFIX + _db + Kingdom.s_SERIALIZATION_PREFIX+ _ki + Group.s_SERIALIZATION_PREFIX + _gp + SubGroup.s_SERIALIZATION_PREFIX + _sg + s_SERIALIZATION_PREFIX + _name;
        final File file = new File(Options.getSerializeDirectory() + File.separator + fileName + Options.getDateModifSerializeExtension());
        final ObjectInputStream stream;
        if (!file.exists()) {
            return null;
        }
        try {
            stream = new ObjectInputStream((new FileInputStream(file)));
            return (Date) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logs.warning("Unable to load : " + fileName);
            Logs.exception(e);
        }
        return null;
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
        return m_parent.getGroupName();
    }

    /**
     * Get the Kingdom's name
     *
     * @return the Kingdom's name
     */
    public String getKingdomName() {
        return m_parent.getKingdomName();
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
        if (m_parent == null)
            throw new InvalidStateException("Unable to start without been add in a SubGroup : " + getName());
        super.start();
    }

    /**
     * Save this organism
     */
    @Override
    public void save() {
        super.save();

        //Saving the Date
        final File file = new File(Options.getSerializeDirectory() + File.separator + getSavedName() + Options.getDateModifSerializeExtension());
        final ObjectOutputStream stream;
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(getModificationDate());
            stream.flush();
            stream.close();
        } catch (IOException e) {
            Logs.warning("Unable to save : " + getSavedName());
            Logs.exception(e);
        }
    }

    /**
     * Get the main part of the save path_name
     *
     * @return the main part of the save path_name
     */
    @Override
    public String getSavedName() {
        return m_parent.getSavedName() + s_SERIALIZATION_PREFIX + getName();
    }

}
