package Data;

import java.util.ArrayList;

public class Organism extends IDataBase{

	/**
	 * Reference to the parent
	 */
	private SubGroup m_parent;
	/**
	 * Array of this organism's replicons
	 */
	private ArrayList<Replicon> m_replicons;
	
	/**
	 * Class constructor
	 * @param _parent, the reference to the parent
	 * @param _name, the name of the organism
	 */
	public Organism(SubGroup _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_replicons = new ArrayList<>();
	}
	
	/**
	 * Get this Organims's Replicons
	 * @return the m_replicons
	 */
	public ArrayList<Replicon> getReplicons(){
		return m_replicons;
	}

	/**
	 * Add a Replicon to this Organism
	 * @param _replicon, the Replicon to add
	 * @return the insertion success
	 */
	public boolean addReplicon(Replicon _replicon){ return m_replicons.add(_replicon);}
}
