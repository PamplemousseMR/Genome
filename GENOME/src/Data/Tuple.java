package Data;

import java.util.EnumMap;

public final class Tuple {

    /**
     * EnumMap to store float
     */
    private final EnumMap<Statistics.StatFloat, Float> m_floats;
    /**
     * EnumMap to store long
     */
    private final EnumMap<Statistics.StatLong, Long> m_longs;

    /**
     * Class constructor
     *
     * @param _floats, the enumMap of float
     * @param _longs,  the enumMap of long
     */
    Tuple(EnumMap<Statistics.StatFloat, Float> _floats, EnumMap<Statistics.StatLong, Long> _longs) {
        m_floats = _floats;
        m_longs = _longs;
    }

    /**
     * Get value from enum
     *
     * @param _stat, the enum
     * @return the float get from the enum
     */
    public Float get(Statistics.StatFloat _stat) {
        return m_floats.get(_stat);
    }

    /**
     * Get value from enum
     *
     * @param _stat, the enum
     * @return the long get from the enum
     */
    public Long get(Statistics.StatLong _stat) {
        return m_longs.get(_stat);
    }

    /**
     * Set value from enum
     *
     * @param _stat, the enum
     * @param _val,  the value to set
     */
    public void set(Statistics.StatFloat _stat, float _val) {
        m_floats.put(_stat, _val);
    }

    /**
     * Set value from enum
     *
     * @param _stat, the enum
     * @param _val,  the value to set
     */
    public void set(Statistics.StatLong _stat, long _val) {
        m_longs.put(_stat, _val);
    }

}
