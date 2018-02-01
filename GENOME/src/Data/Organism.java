package Data;

import java.util.LinkedList;

public class Organism extends IDataBase {

	/**
	 * Reference to the parent
	 */
	private SubGroup m_parent;
	/**
	 * Array of this organism's replicons
	 */
	private LinkedList<Replicon> m_replicons;
	
	/**
	 * Class constructor
	 * @param _name, the name of the organism
	 */
	public Organism(String _name) {
		super(_name);
		m_replicons = new LinkedList<>();
		m_parent = null;
	}

    /**
     * Add a Replicon to this Organisme
     * @param _replicon, the Replicon to insert
     * @return the insertion success
     */
    public boolean addReplicon(Replicon _replicon) throws Exception{
    	if(m_replicons.contains(_replicon))
    		throw new Exception("Replicon already added");
		_replicon.setParent(this);
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
     */
	public boolean finish() throws Exception{
		for(Replicon rep : m_replicons) {
			getStatistics().update(rep.getStatistics());
		}
		m_replicons.clear();
		m_parent.finish(this);
		return true;
    }

    public String getSubGroupName(){
		return m_parent.getName();
	}

	public String getGroupName(){
		return m_parent.getParent().getName();
	}

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
