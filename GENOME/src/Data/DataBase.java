package Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;

import Data.Replicon.Type;

public class DataBase {

	/**
	 * Instance of the Singleton
	 */
	private static DataBase m_dataBase;
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
	private ArrayList<Kingdom> m_kingdoms;
	
	/**
	 * Class constructor
	 * @param _name, the name of the organism
	 * @param _kingdom, the name of the kingdom
	 * @param _group, the name of the group
	 */
	private DataBase() {
		m_modificationDate = new Date();
		m_genomeNumber = new EnumMap<>(Replicon.Type.class);
		for(Type field : Replicon.Type.values()) {
			m_genomeNumber.put(field,0l);
		}
		m_kingdoms = new ArrayList<>();
	}
	
	/**
	 * Accessor to the singleton
	 * @return the instance of the singleton
	 */
	public static DataBase getDataBase() {
		if(m_dataBase == null){
			return (m_dataBase = new DataBase());
		}else {
			return m_dataBase;
		}
	}
	
	/**
	 * Add a organism to the subGroup
	 * @param _organisme, the organism to insert
	 * @return the insertion success
	 */
	public boolean addKingdom(Kingdom _kingdom) {
		return m_kingdoms.add(_kingdom);
	}
	
	public void addReplicon(Replicon.Type _type) {
		m_genomeNumber.put(_type, m_genomeNumber.get(_type)+1);
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
		
}
