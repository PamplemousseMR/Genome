package Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

import Data.Replicon.Type;

public class SubGroup {

	/**
	 * Reference to the parent
	 */
	private Group m_parent;
	/**
	 * Name of the organism
	 */
	private String m_name;
	/**
	 * Kingdom of the organism
	 */
	private String m_kingdom;
	/**
	 * Group of the organism
	 */
	private String m_group;
	/**
	 * Last modification date of the organism
	 */
	private Date m_modificationDate;
	/**
	 * Array of values of each replicon's type (Type.CHROMOSOME = 12, Type.MITOCHONDRION = 10, ...)
	 */
	private EnumMap<Replicon.Type,Long> m_genomeNumber;
	/**
	 * Array of this subgroup's organisms
	 */
	private ArrayList<Organism> m_organisms;
	
	/**
	 * Class constructor
	 * @param _name, the name of the organism
	 * @param _kingdom, the name of the kingdom
	 * @param _group, the name of the group
	 */
	public SubGroup(Group _parent, String _name, String _kingdom, String _group) {
		m_parent = _parent;
		m_name = _name;
		m_kingdom = _kingdom;
		m_group = _group;
		m_modificationDate = new Date();
		m_genomeNumber = new EnumMap<>(Replicon.Type.class);
		for(Type field : Replicon.Type.values()) {
			m_genomeNumber.put(field,0l);
		}
		m_organisms = new ArrayList<>();
	}
	
	/**
	 * Add a organism to the subGroup
	 * @param _organisme, the organism to insert
	 * @return the insertion success
	 */
	public boolean addOrganism(Organism _organisme) {
		return m_organisms.add(_organisme);
	}
	
	public void addReplicon(Replicon.Type _type) {
		m_genomeNumber.put(_type, m_genomeNumber.get(_type)+1);
		m_parent.addReplicon(_type);
	}

	/**
	 * Get the number of a genome's specified type
	 * @param _type, the type of the genomes to get
	 * @return the number of genomes
	 */
	public Long getTypeNumber(Replicon.Type _type) {
		return m_genomeNumber.get(_type);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return m_name;
	}
	
	/**
	 * @return the kingdom
	 */
	public String getKingdom() {
		return m_kingdom;
	}
	
	/**
	 * @return the group
	 */
	public String getGroup() {
		return m_group;
	}
	
	/**
	 * @return the m_modificationDate
	 */
	public Date getModificationDate() {
		return m_modificationDate;
	}
	
	/**
	 * @return the m_genomeNumber
	 */
	public EnumMap<Replicon.Type,Long> getGenomeNumber(){
		return m_genomeNumber;
	}
	
	/**
	 * @return the m_replicons
	 */
	public ArrayList<Organism> getOrganisms(){
		return m_organisms;
	}
	
}
