package Data;

import java.util.ArrayList;

public final class SubGroup extends IDataBase {

	/**
	 * Reference to the parent
	 */
	private Group m_parent;
	/**
	 * Array of this SubGroup's Organisms
	 */
	private ArrayList<Organism> m_organisms;

	/**
	 * Class constructor
	 * @param _name, the name of this SubGroup
	 */
	public SubGroup(String _name) {
		super(_name);
		m_organisms = new ArrayList<>();
		m_parent = null;
	}

	/**
	 * Add an Organism to this SubGroup
	 * @param _organism, the Organism to insert
	 * @return the insertion success
	 * @throws Exception if _organism are already added
	 */
	public boolean addOrganism(Organism _organism) throws Exception{
		if(super.getState()==State.STARTED) {
			if(super.contains(m_organisms,_organism))
				throw new Exception("Organims already added");
			_organism.setIndex(m_organisms.size());
			_organism.setParent(this);
			return m_organisms.add(_organism);
		}else return false;
	}

	/**
	 * In case of all Organisme are already finished
	 * @throws Exception if it can't be sopped
	 */
	@Override
	public void stop() throws Exception{
    	super.stop();
		if(super.getFinishedChildrens() == m_organisms.size()){
			m_organisms.clear();
			super.computeStatistics();
			m_parent.finish(this);
			super.finish();
		}
	}

	/**
	 * Get the Organism of this SubGroup
	 * @return the m_groups
	 */
	public ArrayList<Organism> getOrganisms(){
		return m_organisms;
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
		if(super.contains(m_organisms, _organism) && _organism.getState()!=State.FINISHED){
			for(Statistics stat : _organism.getStatistics().values()){
				super.updateStatistics(stat);
				super.incrementGenomeNumber(stat.getType(),_organism.getTypeNumber(stat.getType()));
			}
			super.incrementFinishedChildrens();
			if(super.getState() == State.STOPPED && super.getFinishedChildrens() == m_organisms.size()){
				m_organisms.clear();
				super.computeStatistics();
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
