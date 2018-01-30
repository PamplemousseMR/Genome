package Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

import Data.Replicon.Type;

public class Kingdom {

	/**
	 * Reference to the parent
	 */
	private DataBase m_parent;
	/**
	 * Name of the organism
	 */
	private String m_name;
	/**
	 * Last modification date of the organism
	 */
	private Date m_modificationDate;
	/**
	 * Array of values of each replicon's type (Type.CHROMOSOME = 12, Type.MITOCHONDRION = 10, ...)
	 */
	private EnumMap<Replicon.Type,Long> m_genomeNumber;
	/**
	 * Array of this group's subGroups
	 */
	private ArrayList<Group> m_groups;
	
	/**
	 * Class constructor
	 * @param _name, the name of the organism
	 * @param _kingdom, the name of the kingdom
	 * @param _group, the name of the group
	 */
	public Kingdom(DataBase _parent, String _name) {
		m_parent = _parent;
		m_name = _name;
		m_modificationDate = new Date();
		m_genomeNumber = new EnumMap<>(Replicon.Type.class);
		for(Type field : Replicon.Type.values()) {
			m_genomeNumber.put(field,0l);
		}
		m_groups = new ArrayList<>();
	}
	
	/**
	 * Add a organism to the subGroup
	 * @param _organisme, the organism to insert
	 * @return the insertion success
	 */
	public boolean addGroup(Group _group) {
		return m_groups.add(_group);
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
	public ArrayList<Group> getGroups(){
		return m_groups;
	}
	
}
