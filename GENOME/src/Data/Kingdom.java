package Data;

import java.util.LinkedList;

public class Kingdom extends IDataBase{

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
	 * @param _parent, this Kingdom's parent
	 * @param _name, the name of this Kingdom
	 */
	public Kingdom(DataBase _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_groups = new LinkedList<>();
		m_parent.addKingdom(this);
	}
	
	/**
	 * Add a Group to this Kingdom
	 * @param _group, the Group to insert
	 * @return the insertion success
	 */
	public boolean addGroup(Group _group) {
		return m_groups.add(_group);
	}
	
	/**
	 * Get the Group of this Kingdom
	 * @return the m_groups
	 */
	public LinkedList<Group> getGroups(){
		return m_groups;
	}

	/**
	 * Update the statistics
	 * @param _stats, the stats to update
	 */
	public void update(Statistics _stats) {
		m_statistics.update(_stats);
		if(getState()== State.DONE && m_groups.size()==0){
			m_parent.getKingdoms().remove(this);
			m_parent.update(m_statistics);
		}
	}
}
