package Data;

import java.util.ArrayList;

public final class Organism extends IState {

	/**
	 * Reference to the parent
	 */
	private SubGroup m_parent;
	/**
	 * Array of this organism's Replicon
	 */
	private ArrayList<Replicon> m_replicons;

	/**
	 * Class constructor
	 * @param _name, the name of the Organism
	 */
	public Organism(String _name) {
		super(_name);
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
        if(getState()==State.STARTED) {
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
	    m_replicons.parallelStream().forEach(rep -> rep.computeStatistic());
		for(Replicon rep : m_replicons) {
 			updateStatistics(rep);
			incrementGenomeNumber(rep.getType());
		}
		m_replicons.clear();
		computeStatistics();
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

	// Do not use

	/**
	 * Set the parent
	 * @param _subGroup, the parent to set
	 */
	protected void setParent(SubGroup _subGroup){
		m_parent = _subGroup;
	}
}
