package Data;

import java.util.ArrayList;

public class Kingdom extends IDataBase{

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
	 * @param _parent, this Kingdom's parent
	 * @param _name, the name of this Kingdom
	 */
	public Kingdom(DataBase _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_groups = new ArrayList<>();
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
	public ArrayList<Group> getGroups(){
		return m_groups;
	}
	
}
