package Data;

import java.util.LinkedList;

public final class Group extends IState {

	/**
	 * Reference to the parent
	 */
	private Kingdom m_parent;
	/**
	 * Array of this Group's SubGroups
	 */
	private LinkedList<SubGroup> m_subGroups;
	
	/**
	 * Class constructor
	 * @param _name, the name of this Group
	 */
	public Group(String _name) {
		super(_name);
		m_subGroups = new LinkedList<>();
		m_parent = null;
	}
	
	/**
	 * Add a SubGroup to this Group
	 * @param _subGroup, the Subgroup to insert
	 * @return the insertion success
	 * @throws Exception if _subGroup are already added
	 */
	public boolean addSubGroup(SubGroup _subGroup) throws Exception {
		if(getState()==State.STARTED) {
			if(m_subGroups.contains(_subGroup))
				throw new Exception("Sequence already added");
			_subGroup.setParent(this);
			return m_subGroups.add(_subGroup);
		}else return false;
	}
	
	/**
	 * Get the Subgroups of this Group
	 * @return the m_subGroups
	 */
	public LinkedList<SubGroup> getSubGroups(){
		return m_subGroups;
	}

	/**
	 * In case of all SubGroup are already finished
	 * @throws Exception if it can't be stopped
	 */
	@Override
	public void stop() throws Exception{
		super.stop();
		if(m_subGroups.size()==0){
			m_parent.finish(this);
			super.finish();
		}
	}

	/**
	 * Get the Kingdom's name
	 * @return the Kingdom's name
	 */
	public String getKingdomName(){
		return m_parent.getName();
	}

	// Do not use

	/**
	 * Finish this Group if it can
	 * @param _subGroup, the SubGroup to finish
	 * @throws Exception if it can't be finished
	 */
	protected void finish(SubGroup _subGroup) throws Exception {
		if(m_subGroups.contains(_subGroup)){
			getStatistics().update(_subGroup.getStatistics());
			m_subGroups.remove(_subGroup);
			if(getState()== State.STOPPED && m_subGroups.size()==0){
				m_parent.finish(this);
				super.finish();
			}
		}
	}

	/**
	 * Set the parent
	 * @param _kingdom, the parent to set
	 */
	protected void setParent(Kingdom _kingdom){
		m_parent = _kingdom;
	}

	/**
	 * Get the Kingdom
	 * @return the Kingdom
	 */
	protected Kingdom getParent(){
		return m_parent;
	}

}
