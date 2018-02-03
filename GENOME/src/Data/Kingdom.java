package Data;

import java.util.LinkedList;

public final class Kingdom extends IState {

	/**
	 * Reference to the parent
	 */
	private DataBase m_parent;
	/**
	 * Array of this Kingdom's Group
	 */
	private LinkedList<Group> m_groups;
	
	/**
	 * Class constructor
	 * @param _name, the name of this Kingdom
	 */
	public Kingdom(String _name) {
		super(_name);
		m_groups = new LinkedList<>();
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
			if(m_groups.contains(_group))
				throw new Exception("Sequence already added");
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
		if(m_groups.size()==0){
			m_parent.finish(this);
			super.finish();
		}
	}
	
	/**
	 * Get the Group of this Kingdom
	 * @return the m_groups
	 */
	public LinkedList<Group> getGroups(){
		return m_groups;
	}

	// Do not use

	/**
	 * Finish this Kingdom if it can
	 * @param _group, the Group to finish
	 * @throws Exception if it can't be finished
	 */
	protected void finish(Group _group) throws Exception {
		if(m_groups.contains(_group)){
			for(Statistics.Type type : Statistics.Type.values()){
				if(_group.getTypeNumber(type) != 0L){
					//todo crée ligne si pas deja fait
					getStatistics(type).update(_group.getStatistics(type));
					incrementGenomeNumber(type,_group.getTypeNumber(type));
				}
			}
			m_groups.remove(_group);
			if(getState()== State.STOPPED && m_groups.size()==0){
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
