package Data;

import java.util.ArrayList;

public final class Organism extends IDataBase {

	/**
	 * Reference to the parent
	 */
	private SubGroup m_parent;
	/**
	 * Array of this organism's Replicon
	 */
	private ArrayList<Replicon> m_replicons;
	/**
	 * The id of this organism
	 */
	private int m_id;
	/**
	 * The version of the organism
	 */
	private int m_version;

	/**
	 * Class constructor
	 * @param _orgRawOrganism, the Organism where get data
	 */
	public Organism(RawOrganism _orgRawOrganism) {
		super(_orgRawOrganism.getName());
		super.setModificationDate(_orgRawOrganism.getModificationDate());
		m_id = _orgRawOrganism.getId();
		m_version = _orgRawOrganism.getVersion();
		m_replicons = new ArrayList<>();
		m_parent = null;
	}

	/**
	 * Add a Replicon to this Organism
	 * @param _replicon, the Replicon to insert
	 * @return the insertion success
	 * @throws Exception if it _replicon are already added
	 */
	public boolean addReplicon(Replicon _replicon) throws Exception{
        if(super.getState()==State.STARTED) {
            try{
                if(m_replicons.get(_replicon.getIndex()) != null)
                    throw new Exception("Replicon already added");
            }catch (IndexOutOfBoundsException e){}
            _replicon.setIndex(m_replicons.size());
            return m_replicons.add(_replicon);
        }else return false;
	}

	/**
	 * Update the statistics
	 * @throws Exception if it can't be finished
	 */
	@Override
	public void finish() throws Exception{
	    m_replicons.parallelStream().forEach(Replicon::computeStatistic);
		for(Replicon rep : m_replicons) {
			super.updateStatistics(rep);
			super.incrementGenomeNumber(rep.getType());
		}
		m_replicons.clear();
		super.computeStatistics();
		m_parent.finish(this);
		super.finish();
	}

    /**
     * Get this Organims's Replicons
     * @return the m_replicons
     */
    public ArrayList<Replicon> getReplicons(){
        return m_replicons;
    }

	/**
	 * Get the SubGroup's name
	 * @return the SubGroup's name
	 */
	public String getSubGroupName(){
		return m_parent.getName();
	}

	/**
	 * Get the Group's name
	 * @return the Group's name
	 */
	public String getGroupName(){
		return m_parent.getParent().getName();
	}

	/**
	 * Get the Kingdom's name
	 * @return the Kingdom's name
	 */
	public String getKingdomName(){
		return m_parent.getParent().getParent().getName();
	}

    /**
     * Get the version of the organism
     * @return the version's number
     */
    public int getVersion() {
        return m_version;
    }

    /**
     * Get the id of the organism
     * @return the id's number
     */
    public int getId() {
        return m_id;
    }

	// Do not use

	/**
	 * Set the parent
	 * @param _subGroup, the parent to set
	 */
	protected void setParent(SubGroup _subGroup){
		m_parent = _subGroup;
	}
}
