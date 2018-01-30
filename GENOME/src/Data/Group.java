package Data;

import java.util.ArrayList;

public class Group extends IDataBase{

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
	 * @param _parent, this Group's parent
	 * @param _name, the name of this Group
	 */
	public Group(Kingdom _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_subGroups = new ArrayList<>();
	}
	
	/**
	 * Add a SubGroup to this Group
	 * @param _subGroup, the Subgroup to insert
	 * @return the insertion success
	 */
	public boolean addSubGroup(SubGroup _subGroup) {
		return m_subGroups.add(_subGroup);
	}
	
	/**
	 * Get the Subgroups of this Group
	 * @return the m_subGroups
	 */
	public ArrayList<SubGroup> getSubGroups(){
		return m_subGroups;
	}
	
}
