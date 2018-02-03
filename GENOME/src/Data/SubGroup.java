package Data;

import java.util.LinkedList;

public final class SubGroup extends IState{

	/**
	 * Reference to the parent
	 */
	private Group m_parent;
	/**
	 * Array of this SubGroup's Organisms
	 */
	private LinkedList<Organism> m_organisms;
	
	/**
	 * Class constructor
	 * @param _name, the name of this SubGroup
	 */
	public SubGroup(String _name) {
		super(_name);
		m_organisms = new LinkedList<>();
		m_parent = null;
	}
	
	/**
	 * Add an Organism to this SubGroup
	 * @param _organism, the Organism to insert
	 * @return the insertion success
	 * @throws Exception if _organism are already added
	 */
	public boolean addOrganism(Organism _organism) throws Exception{
		if(getState()==State.STARTED) {
			if(m_organisms.contains(_organism))
				throw new Exception("Organims already added");
			_organism.setParent(this);
			return m_organisms.add(_organism);
		}else return false;
	}

	/**
	 * Get the Organism of this SubGroup
	 * @return the m_groups
	 */
	public LinkedList<Organism> getOrganisms(){
		return m_organisms;
	}

	/**
	 * In case of all Organisme are already finished
	 * @throws Exception if it can't be sopped
	 */
	@Override
	public void stop() throws Exception{
    	super.stop();
		if(m_organisms.size()==0){
			m_parent.finish(this);
			super.finish();
		}
	}

	/**
	 * Get the Group's name
	 * @return the Group's name
	 */
	public String getGroupName(){
		return m_parent.getName();
	}

	/**
	 * Get the Kingdom's name
	 * @return the Kingdom's name
	 */
	public String getKingdomName(){
		return m_parent.getParent().getName();
	}

	// Do not use

	/**
	 * Finish this Subgroup if it can
	 * @param _organism, the Organism to finish
	 * @throws Exception if it can't be finished
	 */
	protected void finish(Organism _organism) throws Exception {
		if(m_organisms.contains(_organism)){
			getStatistics().update(_organism.getStatistics());
			m_organisms.remove(_organism);
			if(getState() == State.STOPPED && m_organisms.size()==0){
				m_parent.finish(this);
				super.finish();
			}
		}
	}

	/**
	 * Set the parent
	 * @param _group, the parent to set
	 */
	protected void setParent(Group _group){
		m_parent = _group;
	}

	/**
	 * Get the Group
	 * @return the Group
	 */
	protected Group getParent(){
		return m_parent;
	}

}
