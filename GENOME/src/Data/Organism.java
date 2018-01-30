package Data;

import java.util.LinkedList;

public class Organism extends IDataBase{

	/**
	 * Reference to the parent
	 */
	private SubGroup m_parent;
	/**
	 * Array of this organism's replicons
	 */
	private LinkedList<Replicon> m_replicons;
	
	/**
	 * Class constructor
	 * @param _parent, the reference to the parent
	 * @param _name, the name of the organism
	 */
	public Organism(SubGroup _parent, String _name) {
		super(_name);
		m_parent = _parent;
		m_replicons = new LinkedList<>();
		m_parent.addOrganism(this);
	}

    /**
     * Add a Replicon to this Organisme
     * @param _replicon, the Replicon to insert
     * @return the insertion success
     */
    public boolean addReplicon(Replicon _replicon) {
        return m_replicons.add(_replicon);
    }
	
	/**
	 * Get this Organims's Replicons
	 * @return the m_replicons
	 */
	public LinkedList<Replicon> getReplicons(){
		return m_replicons;
	}

    /**
     * Update the statistics
     * @param _stats, the stats to update
     */
    public void update(Statistics _stats) {
        m_statistics.update(_stats);
        if(getState()== State.DONE && m_replicons.size()==0){
            m_parent.getOrganisms().remove(this);
            m_parent.update(m_statistics);
        }
    }
}
