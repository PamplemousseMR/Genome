package Data;

import java.util.LinkedList;

public class Kingdom extends IState {

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
	 */
	public boolean addGroup(Group _group) {
        if(getState()==State.STARTED) {
            _group.setParent(this);
            return m_groups.add(_group);
        }else return false;
    }
	
	/**
	 * Get the Group of this Kingdom
	 * @return the m_groups
	 */
	public LinkedList<Group> getGroups(){
		return m_groups;
	}

    /**
     * In case of all Group are already finished
     */
    @Override
    public void stop() throws Exception{
        super.stop();
        if(m_groups.size()==0){
            m_parent.finish(this);
			super.finish();
        }
    }

	// Do not use

	/**
	 * Finish this Kingdom if it can
	 * @param _group, the Group to finish
	 */
	protected boolean finish(Group _group) throws Exception {
		if(m_groups.contains(_group)){
			getStatistics().update(_group.getStatistics());
			m_groups.remove(_group);
			if(getState()== State.STOPPED && m_groups.size()==0){
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
	 * @param _dataBase, the parent to set
	 */
	protected void setParent(DataBase _dataBase){
		m_parent = _dataBase;
	}

}
