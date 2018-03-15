package Data;

import java.util.Arrays;

public final class Tuple {

    /**
     * Array to store float
     */
    private final Float[] m_floats;
    /**
     * Array to store long
     */
    private final Long[] m_longs;

    /**
     * Class constructor
     */
    Tuple() {
        m_longs = new Long[Statistics.StatLong.values().length];
        Arrays.fill(m_longs, 0L);
        m_floats = new Float[Statistics.StatFloat.values().length];
        Arrays.fill(m_floats, 0.F);
    }

    /**
     * Get value from the enum
     *
     * @param _stat, the enum
     * @return the float get from the enum
     */
    public Float get(Statistics.StatFloat _stat) {
        return m_floats[_stat.ordinal()];
    }

    /**
     * Get value from the enum
     *
     * @param _stat, the enum
     * @return the long get from the enum
     */
    public Long get(Statistics.StatLong _stat) {
        return m_longs[_stat.ordinal()];
    }

    /**
     * Set value of the enum
     *
     * @param _stat, the enum
     * @param _val,  the value to set
     */
    protected void set(Statistics.StatFloat _stat, float _val) {
        m_floats[_stat.ordinal()] = _val;
    }

    /**
     * Set value of the enum
     *
     * @param _stat, the enum
     * @param _val,  the value to set
     */
    protected void set(Statistics.StatLong _stat, long _val) {
        m_longs[_stat.ordinal()] = _val;
    }

    /**
     * Increment the value of the enum
     *
     * @param _stat, the enum
     * @param _val,  the value to set
     */
    protected void incr(Statistics.StatLong _stat, long _val) {
        m_longs[_stat.ordinal()] += _val;
    }

}
