package Data;

import java.util.LinkedList;

public class SubGroup extends IDataBase{

	/**
	 * Reference to the parent
	 */
	private Group m_parent;
	/**
	 * Array of this SubGroup's Organisms
	 */
	private LinkedList<Organism> m_organisms;
	
	/**
	 * Class constructor
	 * @param _parent, this SubGroup's parent
	 * @param _name, the name of this SubGroup
	 */
	public SubGroup(Group _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_organisms = new LinkedList<>();
		m_parent.addSubGroup(this);
	}
	
	/**
	 * Add an Organism to this SubGroup
	 * @param _organism, the Organism to insert
	 * @return the insertion success
	 */
	public boolean addOrganism(Organism _organism) {
		return m_organisms.add(_organism);
	}
	
	/**
	 * Get the Organism of this SubGroup
	 * @return the m_groups
	 */
	public LinkedList<Organism> getOrganisms(){
		return m_organisms;
	}

    /**
     * Update the statistics
     * @param _stats, the stats to update
     */
    public void update(Statistics _stats) {
        m_statistics.update(_stats);
        if(getState()== State.DONE && m_organisms.size()==0){
            m_parent.getSubGroups().remove(this);
            m_parent.update(m_statistics);
        }
    }
}
