package Data;

import java.util.ArrayList;

public class SubGroup extends IDataBase{

	/**
	 * Reference to the parent
	 */
	private Group m_parent;
	/**
	 * Array of this SubGroup's Organisms
	 */
	private ArrayList<Organism> m_organisms;
	
	/**
	 * Class constructor
	 * @param _parent, this SubGroup's parent
	 * @param _name, the name of this SubGroup
	 */
	public SubGroup(Group _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_organisms = new ArrayList<>();
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
	public ArrayList<Organism> getOrganisms(){
		return m_organisms;
	}
	
}
