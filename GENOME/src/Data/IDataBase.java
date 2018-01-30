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
     * Array of values of each Replicon's type
     */
    private EnumMap<Replicon.Type,Long> m_genomeNumber;

    /**
     * Class constructor
     */
    protected IDataBase(String _name){
        m_name = _name;
        m_modificationDate = new Date();
        m_genomeNumber = new EnumMap<>(Replicon.Type.class);
        for(Replicon.Type field : Replicon.Type.values()) {
            m_genomeNumber.put(field,0l);
        }
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
     * Get the number of a genome's specified type
     * @param _type, the Type of the genomes's number to get
     * @return the number of genomes
     */
    public Long getTypeNumber(Replicon.Type _type) {
        return m_genomeNumber.get(_type);
    }

    /**
     * Get the map of each genome's Type
     * @return the m_genomeNumber
     */
    public EnumMap<Replicon.Type,Long> getGenomeNumber(){
        return m_genomeNumber;
    }
}
