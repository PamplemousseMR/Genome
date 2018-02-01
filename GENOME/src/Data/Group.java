package Data;

import java.util.LinkedList;

public class Group extends IState {

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
	 */
	public boolean addSubGroup(SubGroup _subGroup) {
		if(getState()==State.STARTED) {
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
	 */
	@Override
	public void stop() throws Exception{
		super.stop();
		if(m_subGroups.size()==0){
			m_parent.finish(this);
			super.finish();
		}
	}

	// Do not use

	/**
	 * Finish this Group if it can
	 * @param _subGroup, the SubGroup to finish
	 */
	protected boolean finish(SubGroup _subGroup) throws Exception {
		if(m_subGroups.contains(_subGroup)){
			getStatistics().update(_subGroup.getStatistics());
			m_subGroups.remove(_subGroup);
			if(getState()== State.STOPPED && m_subGroups.size()==0){
				m_parent.finish(this);
				super.finish();
				return true;
			}else{
				return false;
			}
		}else {
			return false;
		}
	}

	/**
	 * Set the parent
	 * @param _kingdom, the parent to set
	 */
	protected void setParent(Kingdom _kingdom){
		m_parent = _kingdom;
	}

}
