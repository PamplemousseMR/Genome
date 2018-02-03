package Data;

import java.util.Date;
import java.util.EnumMap;

public class IDataBase {

    /**
     * The name
     */
    private String m_name;
    /**
     * Last modification's date
     */
    private Date m_modificationDate;
    /**
     * Statistics of this IDataBase
     */
    private EnumMap<Statistics.Type,Statistics> m_statistics;
    /**
     * Array of values of each Replicon's type
     */
    private EnumMap<Statistics.Type,Long> m_genomeNumber;

    /**
     * Class constructor
     */
    IDataBase(String _name){
        m_name = _name;
        m_modificationDate = new Date();
        m_statistics = new EnumMap<>(Statistics.Type.class);
        m_genomeNumber = new EnumMap<>(Statistics.Type.class);
    }

    /**
     * Get the last modification's date
     * @return the m_modificationDate
     */
    public Date getModificationDate() {
        return m_modificationDate;
    }

    /**
     * Get the name
     * @return the m_name
     */
    public String getName(){
        return m_name;
    }

    /**
     * Get the statistics
     * @return the statistics
     */
    public EnumMap<Statistics.Type,Statistics> getStatistics() {
         return m_statistics;
    }

    /**
     * Get number of each Genome's Type
     * @return the number of each Genome's Type
     */
    public EnumMap<Statistics.Type, Long> getGenomeNumber() {
        return m_genomeNumber;
    }

    // Do not used

    /**
     * Get the number of a genome's specified type
     * @param _type, the Type of the genomes's number to get
     * @return the number of genomes
     */
    Long getTypeNumber(Statistics.Type _type) {
        return m_genomeNumber.get(_type);
    }

    /**
     * Increment by 1 the number of genome to a type
     * @param _type, the Type of the genomes to increment
     */
    void incrementGenomeNumber(Statistics.Type _type) {
        if(m_genomeNumber.get(_type) == null){
            m_genomeNumber.put(_type, 1L);
        }else {
            m_genomeNumber.put(_type, m_genomeNumber.get(_type) + 1L);
        }
    }

    /**
     * Increment by _incr the number of genome of a type
     * @param _type, the Type of the genomes to increment
     * @param _incr, the value of the increment
     */
    void incrementGenomeNumber(Statistics.Type _type,long _incr){
        if(m_genomeNumber.get(_type) == null){
            m_genomeNumber.put(_type, _incr);
        }else{
            m_genomeNumber.put(_type,m_genomeNumber.get(_type)+_incr);
        }
    }

    void updateStatistics(Statistics _statistics){
        if(m_statistics.get(_statistics.getType()) == null){
            m_statistics.put(_statistics.getType(), new Statistics(_statistics.getType()));
        }
            m_statistics.get(_statistics.getType()).update(_statistics);
    }

    void computeStatistics(){
        for(Statistics stat : m_statistics.values()){
            stat.compute();
        }
    }

}

