package Data;

import java.util.ArrayList;

public final class Group extends IState {

	/**
	 * Reference to the parent
	 */
	private Kingdom m_parent;
	/**
	 * Array of this Group's SubGroups
	 */
	private ArrayList<SubGroup> m_subGroups;

	/**
	 * Class constructor
	 * @param _name, the name of this Group
	 */
	public Group(String _name) {
		super(_name);
		m_subGroups = new ArrayList<>();
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
			if(contains(m_subGroups,_subGroup))
				throw new Exception("Sequence already added");
			_subGroup.setIndex(m_subGroups.size());
			_subGroup.setParent(this);
			return m_subGroups.add(_subGroup);
		}else return false;
	}

	/**
	 * In case of all SubGroup are already finished
	 * @throws Exception if it can't be stopped
	 */
	@Override
	public void stop() throws Exception{
		super.stop();
		if(getFinishedChildrens() == m_subGroups.size()){
			m_subGroups.clear();
			computeStatistics();
			m_parent.finish(this);
			super.finish();
		}
	}

	/**
	 * Get the Subgroups of this Group
	 * @return the m_subGroups
	 */
	public ArrayList<SubGroup> getSubGroups(){
		return m_subGroups;
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
		if(contains(m_subGroups,_subGroup) && _subGroup.getState()!=State.FINISHED){
            for(Statistics stat : _subGroup.getStatistics().values()){
                updateStatistics(stat);
                incrementGenomeNumber(stat.getType(),_subGroup.getTypeNumber(stat.getType()));
            }
			incrementFinishedChildrens();
			if(getState()== State.STOPPED && getFinishedChildrens() == m_subGroups.size()){
				m_subGroups.clear();
				computeStatistics();
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
