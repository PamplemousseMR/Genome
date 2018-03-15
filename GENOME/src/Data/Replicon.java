package Data;

import Exception.AddException;

import java.util.ArrayList;

public final class Replicon extends Statistics {

    /**
     * The name
     */
    private final String m_name;
    /**
     * Array of all the sequences of this Replicon
     */
    private final ArrayList<StringBuilder> m_sequences;
    /**
     * Local index
     */
    private int m_index;

    /**
     * Class constructor
     *
     * @param _type, the type of this Replicon
     * @param _name, the name of the organism
     */
    public Replicon(Type _type, String _name) {
        super(_type);
        m_name = _name;
        m_sequences = new ArrayList<>();
        m_index = -1;
    }

    /**
     * Get the name
     *
     * @return the m_name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Add a sequence
     *
     * @param _sequence, the sequence to add
     * @return the insertion success
     * @throws AddException if the _sequence are already added
     */
    public boolean addSequence(StringBuilder _sequence) throws AddException {
        if (m_sequences.contains(_sequence))
            throw new AddException("Sequence already added : " + _sequence);
        return m_sequences.add(_sequence);
    }

    // Do not use

    /**
     * Compute statistics of this Replicon
     */
    protected void computeStatistic() {
        int idx, length;
        for (StringBuilder sequence : m_sequences) {
            final Statistics temp = new Statistics(getType());
            length = sequence.length();
            idx = 0;
            while (length - idx > 5) {
                temp.incrementStat(Trinucleotide.valueOf(sequence.substring(idx, idx + 3)), StatLong.PHASE0);
                temp.incrementStat(Trinucleotide.valueOf(sequence.substring(idx + 2, idx + 5)), StatLong.PHASE2);
                temp.incrementStat(Trinucleotide.valueOf(sequence.substring(idx + 1, idx + 4)), StatLong.PHASE1);
                idx += 3;
            }
            super.incrementTotal(idx / 3);
            super.incrementValidCDS(1L);

            long val0, val1, val2;
            for (Tuple tuple : temp.getTable()) {
                val0 = tuple.get(Statistics.StatLong.PHASE0);
                val1 = tuple.get(Statistics.StatLong.PHASE1);
                val2 = tuple.get(Statistics.StatLong.PHASE2);
                if (val0 < val1 || val0 < val2)
                    val0 = 0;
                if (val1 < val0 || val1 < val2)
                    val1 = 0;
                if (val2 < val0 || val2 < val1)
                    val2 = 0;
                if (val0 != 0)
                    tuple.set(Statistics.StatLong.PREF0, 1);
                if (val1 != 0)
                    tuple.set(Statistics.StatLong.PREF1, 1);
                if (val2 != 0)
                    tuple.set(Statistics.StatLong.PREF2, 1);
            }
            update(temp);
            super.compute();
        }
    }

    /**
     * Get the local index
     *
     * @return the local index
     */
    protected int getIndex() {
        return m_index;
    }

    /**
     * Set the local index
     *
     * @param _id, the index to set
     */
    protected void setIndex(int _id) {
        m_index = _id;
    }

}
