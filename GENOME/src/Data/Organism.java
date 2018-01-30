package Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

import Data.Replicon.Type;

public class Organism {

	/**
	 * Reference to the parent
	 */
	private SubGroup m_parent;
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
	 * Subgroup of the organism
	 */
	private String m_subgroup;
	/**
	 * Last modification date of the organism
	 */
	private Date m_modificationDate;
	/**
	 * Array of values of each replicon's type (Type.CHROMOSOME = 12, Type.MITOCHONDRION = 10, ...)
	 */
	private EnumMap<Replicon.Type,Long> m_genomeNumber;
	/**
	 * Array of this organism's replicons
	 */
	private ArrayList<Replicon> m_replicons;
	
	/**
	 * Class constructor
	 * @param _parent, the reference to the parent
	 * @param _name, the name of the organism
	 * @param _kingdom, the name of the kingdom
	 * @param _group, the name of the group
	 * @param _subgroup, the name of the subgroup
	 */
	public Organism(SubGroup _parent, String _name, String _kingdom, String _group, String _subgroup) {
		m_parent = _parent;
		m_name = _name;
		m_kingdom = _kingdom;
		m_group = _group;
		m_subgroup = _subgroup; 
		m_modificationDate = new Date();
		m_genomeNumber = new EnumMap<>(Replicon.Type.class);
		for(Type field : Replicon.Type.values()) {
			m_genomeNumber.put(field,0l);
		}
		m_replicons = new ArrayList<>();
	}
	
	/**
	 * Add Replicon to m_replicon
	 * @param _replicon the Replicon to add
	 * @return the insertion success
	 */
	public boolean addReplicon(Replicon _replicon) {
		if(m_replicons.add(_replicon)) {
			m_genomeNumber.put(_replicon.getType(), m_genomeNumber.get(_replicon.getType())+1);
			m_parent.addReplicon(_replicon.getType());
			return true;
		}else {
			return false;
		}
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
	 * @return the subgroup
	 */
	public String getSubgroup() {
		return m_subgroup;
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
	public ArrayList<Replicon> getReplicons(){
		return m_replicons;
	}
}
