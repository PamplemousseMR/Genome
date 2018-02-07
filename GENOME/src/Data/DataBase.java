package Data;

import java.util.ArrayList;

public final class DataBase extends IDataBase {

	/**
	 * Name of the database
	 */
	public static final String s_NAME = "GENOME";
	/**
	 * Instance of the singleton
	 */
	private static DataBase s_DataBase;
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
	public static DataBase getInstance() {
		if(s_DataBase == null){
			return (s_DataBase = new DataBase(s_NAME));
		}else {
			return s_DataBase;
		}
	}

	/**
	 * Add a Kingdom to the Database
	 * @param _kingdom, the Kingdom to insert
	 * @return the insertion success
	 * @throws Exception if _kingdom are already added
	 */
	public boolean addKingdom(Kingdom _kingdom) throws Exception {
		if(super.getState()==State.STARTED) {
			if(super.contains(m_kingdoms, _kingdom))
				throw new Exception("Sequence already added");
			_kingdom.setIndex(m_kingdoms.size());
			_kingdom.setParent(this);
			return m_kingdoms.add(_kingdom);
		}else return false;
	}

	/**
	 * Get the Kingdoms of this DataBase
	 * @return the m_kingdoms
	 */
	public ArrayList<Kingdom> getKingdoms(){
		return m_kingdoms;
	}

	/**
	 * In case of all Kingdom are already finished
	 * @throws Exception if it can't be stopped
	 */
	@Override
	public void stop() throws Exception{
		super.stop();
		if(super.getFinishedChildrens() == m_kingdoms.size()){
            m_kingdoms.clear();
			super.computeStatistics();
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
		if(super.contains(m_kingdoms, _kingdom) && _kingdom.getState()!=State.FINISHED){
            for(Statistics stat : _kingdom.getStatistics().values()){
				super.updateStatistics(stat);
				super.incrementGenomeNumber(stat.getType(),_kingdom.getTypeNumber(stat.getType()));
            }
            incrementFinishedChildrens();
            if(super.getState()== IDataBase.State.STOPPED && super.getFinishedChildrens() == m_kingdoms.size()){
                m_kingdoms.clear();
				super.computeStatistics();
				super.finish();
			}
		}
	}

}
