package Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

import Data.Replicon.Type;

public class Organism {

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
	 * Array of this organism NCs
	 */
	private ArrayList<Replicon> m_replicon;
	
	/**
	 * Class constructor
	 * @param _name, the name of the organism
	 */
	public Organism(String _name, String _kingdom, String _group, String _subgroup) {
		m_name = _name;
		m_kingdom = _kingdom;
		m_group = _group;
		m_subgroup = _subgroup;
		m_modificationDate = new Date();
		m_genomeNumber = new EnumMap<>(Replicon.Type.class);
		for(Type field : Replicon.Type.values()) {
			m_genomeNumber.put(field,0l);
		}
		m_replicon = new ArrayList<>();
	}
	
	/**
	 * Add NC to m_ncs
	 * @param _nc the NC to add
	 * @return the insertion success
	 */
	public boolean addNC(Replicon _nc) {
		if(m_replicon.add(_nc)) {
			m_genomeNumber.put(_nc.getType(), m_genomeNumber.get(_nc.getType())+1);
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
	 * @return the m_ncs
	 */
	public ArrayList<Replicon> getReplicon(){
		return m_replicon;
	}
}
