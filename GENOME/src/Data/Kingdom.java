package Data;

import java.util.ArrayList;

public final class Kingdom extends IState {

	/**
	 * Reference to the parent
	 */
	private DataBase m_parent;
	/**
	 * Array of this Kingdom's Group
	 */
	private ArrayList<Group> m_groups;

	/**
	 * Class constructor
	 * @param _name, the name of this Kingdom
	 */
	public Kingdom(String _name) {
		super(_name);
		m_groups = new ArrayList<>();
		m_parent = null;
	}

	/**
	 * Add a Group to this Kingdom
	 * @param _group, the Group to insert
	 * @return the insertion success
	 * @throws Exception if _group are already added
	 */
	public boolean addGroup(Group _group) throws Exception {
        if(getState()==State.STARTED) {
			if(contains(m_groups,_group))
				throw new Exception("Sequence already added");
			_group.setIndex(m_groups.size());
			_group.setParent(this);
            return m_groups.add(_group);
        }else return false;
    }

	/**
	 * In case of all Group are already finished
	 * @throws Exception if it can't be stopped
	 */
	@Override
	public void stop() throws Exception{
		super.stop();
		if(getFinishedChildrens() == m_groups.size()){
			m_groups.clear();
			computeStatistics();
			m_parent.finish(this);
			super.finish();
		}
	}

	/**
	 * Get the Group of this Kingdom
	 * @return the m_groups
	 */
	public ArrayList<Group> getGroups(){
		return m_groups;
	}

	// Do not use

	/**
	 * Finish this Kingdom if it can
	 * @param _group, the Group to finish
	 * @throws Exception if it can't be finished
	 */
	protected void finish(Group _group) throws Exception {
		if(contains(m_groups, _group) && _group.getState()!=State.FINISHED){
            for(Statistics stat : _group.getStatistics().values()){
                updateStatistics(stat);
                incrementGenomeNumber(stat.getType(),_group.getTypeNumber(stat.getType()));
            }
			incrementFinishedChildrens();
			if(getState()== State.STOPPED && getFinishedChildrens() == m_groups.size()){
				m_groups.clear();
				computeStatistics();
				m_parent.finish(this);
				super.finish();
			}
		}
	}

	/**
	 * Set the parent
	 * @param _dataBase, the parent to set
	 */
	protected void setParent(DataBase _dataBase){
		m_parent = _dataBase;
	}

}
