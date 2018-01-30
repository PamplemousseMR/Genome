package Data;

import java.util.ArrayList;

public class DataBase extends IDataBase{

	/**
	 * Instance of the singleton
	 */
	private static DataBase m_dataBase;
	/**
	 * Array of this Database's Kingdom
	 */
	private ArrayList<Kingdom> m_kingdoms;
	
	/**
	 * Class constructor
	 * @param _name, the name of this database
	 */
	private DataBase(String _name) {
		super(_name);
		m_kingdoms = new ArrayList<>();
	}
	
	/**
	 * Accessor to the singleton
	 * @return the instance of the singleton
	 */
	public static DataBase getDataBase() {
		if(m_dataBase == null){
			return (m_dataBase = new DataBase("GENOME"));
		}else {
			return m_dataBase;
		}
	}
	
	/**
	 * Add a Kingdom to the Database
	 * @param _kingdom, the Kingdom to insert
	 * @return the insertion success
	 */
	public boolean addKingdom(Kingdom _kingdom) {
		return m_kingdoms.add(_kingdom);
	}

	/**
	 * Get the Kingdoms of this DataBase
	 * @return the m_kingdoms
	 */
	public ArrayList<Kingdom> getKingdoms(){
		return m_kingdoms;
	}

}
