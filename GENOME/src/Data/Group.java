package Data;

import java.util.LinkedList;

public class Group extends IDataBase{

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
	 * @param _parent, this Group's parent
	 * @param _name, the name of this Group
	 */
	public Group(Kingdom _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_subGroups = new LinkedList<>();
		m_parent.addGroup(this);
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
	public LinkedList<SubGroup> getSubGroups(){
		return m_subGroups;
	}

	/**
	 * Update the statistics
	 * @param _stats, the stats to update
	 */
	public void update(Statistics _stats) {
		m_statistics.update(_stats);
		if(getState()== State.DONE && m_subGroups.size()==0){
			m_parent.getGroups().remove(this);
			m_parent.update(m_statistics);
		}
	}
}
