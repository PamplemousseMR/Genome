package Data;

import java.util.LinkedList;

public final class DataBase extends IState {

	public static final String s_NAME = "GENOME";
	/**
	 * Instance of the singleton
	 */
	private static DataBase m_dataBase;
	/**
	 * Array of this Database's Kingdom
	 */
	private LinkedList<Kingdom> m_kingdoms;
	
	/**
	 * Class constructor
	 * @param _name, the name of this database
	 */
	private DataBase(String _name) {
		super(_name);
		m_kingdoms = new LinkedList<>();
	}
	
	/**
	 * Accessor to the singleton
	 * @return the instance of the singleton
	 */
	public static DataBase getInstance() {
		if(m_dataBase == null){
			return (m_dataBase = new DataBase(s_NAME));
		}else {
			return m_dataBase;
		}
	}
	
	/**
	 * Add a Kingdom to the Database
	 * @param _kingdom, the Kingdom to insert
	 * @return the insertion success
	 * @throws Exception if _kingdom are already added
	 */
	public boolean addKingdom(Kingdom _kingdom) throws Exception {
		if(getState()==State.STARTED) {
			if(m_kingdoms.contains(_kingdom))
				throw new Exception("Sequence already added");
			_kingdom.setParent(this);
			return m_kingdoms.add(_kingdom);
		}else return false;
	}

	/**
	 * Get the Kingdoms of this DataBase
	 * @return the m_kingdoms
	 */
	public LinkedList<Kingdom> getKingdoms(){
		return m_kingdoms;
	}

	/**
	 * In case of all Kingdom are already finished
	 * @throws Exception if it can't be stopped
	 */
	@Override
	public void stop() throws Exception{
		super.stop();
		if(m_kingdoms.size()==0){
			super.finish();
		}
	}

	// Do not use

	/**
	 * Finish this DataBase if it can
	 * @param _kingdom, the Kingdom to finish
	 * @throws Exception if it can't be finished
	 */
	protected void finish(Kingdom _kingdom) throws Exception {
		if(m_kingdoms.contains(_kingdom)){
			for(Statistics.Type type : Statistics.Type.values()){
				if(_kingdom.getTypeNumber(type) != 0L){
					//todo crée ligne si pas deja fait
					getStatistics(type).update(_kingdom.getStatistics(type));
					incrType(type,_kingdom.getTypeNumber(type));
				}
			}
			m_kingdoms.remove(_kingdom);
			if(getState()== IState.State.STOPPED && m_kingdoms.size()==0){
				super.finish();
			}
		}
	}

}
