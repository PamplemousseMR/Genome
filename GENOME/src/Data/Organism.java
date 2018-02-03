package Data;

import java.util.LinkedList;

public final class Organism extends IDataBase {

	/**
	 * Reference to the parent
	 */
	private SubGroup m_parent;
	/**
	 * Array of this organism's Replicon
	 */
	private LinkedList<Replicon> m_replicons;
	
	/**
	 * Class constructor
	 * @param _name, the name of the Organism
	 */
	public Organism(String _name) {
		super(_name);
		m_replicons = new LinkedList<>();
		m_parent = null;
	}

	/**
	 * Add a Replicon to this Organism
	 * @param _replicon, the Replicon to insert
	 * @return the insertion success
	 * @throws Exception if it _replicon are already added
	 */
	public boolean addReplicon(Replicon _replicon) throws Exception{
		if(m_replicons.contains(_replicon))
			throw new Exception("Replicon already added");
		return m_replicons.add(_replicon);
	}
	
	/**
	 * Get this Organims's Replicons
	 * @return the m_replicons
	 */
	public LinkedList<Replicon> getReplicons(){
		return m_replicons;
	}

	/**
	 * Update the statistics
	 * @throws Exception if it can't be finished
	 */
	public boolean finish() throws Exception{
		for(Replicon rep : m_replicons) {
			rep.computeStatistic();
			updateStatistics(rep);
			incrementGenomeNumber(rep.getType());
		}
		m_replicons.clear();
		computeStatistics();
		m_parent.finish(this);
		return true;
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

	// Do not use

	/**
	 * Set the parent
	 * @param _subGroup, the parent to set
	 */
	protected void setParent(SubGroup _subGroup){
		m_parent = _subGroup;
	}
}
