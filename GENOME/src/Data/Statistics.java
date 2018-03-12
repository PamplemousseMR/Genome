package Data;

import java.util.EnumMap;
import java.util.stream.IntStream;

public class Statistics {

    /**
     * Type of this Statistic
     */
    private final Type m_type;
    /**
     * Array to store statistics
     */
    private final EnumMap<Trinucleotide, Tuple> m_trinucleotideTable;
    /**
     * Number total of trinucleotide on phase 0
     */
    private long m_totalTrinucleotide;

    /**
     * Class constructor
     */
    Statistics(Type _type) {
        m_type = _type;
        m_trinucleotideTable = new EnumMap<>(Trinucleotide.class);
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final EnumMap<StatFloat, Float> arrf = new EnumMap<>(StatFloat.class);
            for (StatFloat stat : StatFloat.values()) {
                arrf.put(stat, 0F);
            }
            final EnumMap<StatLong, Long> arrl = new EnumMap<>(StatLong.class);
            for (StatLong stat : StatLong.values()) {
                arrl.put(stat, 0L);
            }
            m_trinucleotideTable.put(Trinucleotide.values()[i], new Tuple(arrf, arrl));
        });
        m_totalTrinucleotide = 0;
    }

    /**
     * Get the type of this Replicon
     *
     * @return the type
     */
    public final Type getType() {
        return m_type;
    }

    /**
     * get the total trinucleotide of the phase 0 number
     *
     * @return the m_TotalTriPhase0
     */
    public final long getTotalTrinucleotide() {
        return m_totalTrinucleotide;
    }

    /**
     * @return the m_trinucleotideTable
     */
    public final EnumMap<Trinucleotide, Tuple> getTable() {
        return m_trinucleotideTable;
    }

    /**
     * Update statistics
     *
     * @param _stats, the stats use to update
     */
    protected final void update(Statistics _stats) {
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final Trinucleotide tri = Trinucleotide.values()[i];
            final Tuple inputRow = _stats.m_trinucleotideTable.get(tri);
            incrementStat(tri, StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            incrementStat(tri, StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            incrementStat(tri, StatLong.PHASE2, inputRow.get(StatLong.PHASE2));
            incrementStat(tri, StatLong.PREF0, inputRow.get(StatLong.PREF0));
            incrementStat(tri, StatLong.PREF1, inputRow.get(StatLong.PREF1));
            incrementStat(tri, StatLong.PREF2, inputRow.get(StatLong.PREF2));
        });
        m_totalTrinucleotide += _stats.m_totalTrinucleotide;
    }

    /**
     * Compute the frequencies and the preferences of each trinucleotide for each phases
     */
    protected final void compute() {
        for (Tuple row : m_trinucleotideTable.values()) {
            row.set(StatFloat.FREQ0, row.get(StatLong.PHASE0) / (float) m_totalTrinucleotide);
            row.set(StatFloat.FREQ1, row.get(StatLong.PHASE1) / (float) m_totalTrinucleotide);
            row.set(StatFloat.FREQ2, row.get(StatLong.PHASE2) / (float) m_totalTrinucleotide);
        }
    }

    /**
     * Increment by 1 the value of a trinucleotide for a stat
     *
     * @param _tri,  the Trinucleotide to set
     * @param _stat, the statistic to set
     */
    protected final void incrementStat(Trinucleotide _tri, StatLong _stat) {
        m_trinucleotideTable.get(_tri).set(_stat, m_trinucleotideTable.get(_tri).get(_stat) + 1);
    }

    /**
     * Increment the value of total trinucleotide by the parameter
     *
     * @param _inc, the value to increment
     */
    protected final void incrementTotal(long _inc) {
        m_totalTrinucleotide += _inc;
    }

    // Do not use

    /**
     * Increment the value of a trinucleotide for a stat by the parameter
     *
     * @param _tri,  the Trinucleotide to set
     * @param _stat, the statistic to set
     * @param _inc,  the value to increment
     */
    private void incrementStat(Trinucleotide _tri, StatLong _stat, long _inc) {
        m_trinucleotideTable.get(_tri).set(_stat, m_trinucleotideTable.get(_tri).get(_stat) + _inc);
    }

    /**
     * List of the 64 trinucleotide
     */
    public enum Trinucleotide {
        AAA,
        AAC,
        AAG,
        AAT,
        ACA,
        ACC,
        ACG,
        ACT,
        AGA,
        AGC,
        AGG,
        AGT,
        ATA,
        ATC,
        ATG,
        ATT,
        CAA,
        CAC,
        CAG,
        CAT,
        CCA,
        CCC,
        CCG,
        CCT,
        CGA,
        CGC,
        CGG,
        CGT,
        CTA,
        CTC,
        CTG,
        CTT,
        GAA,
        GAC,
        GAG,
        GAT,
        GCA,
        GCC,
        GCG,
        GCT,
        GGA,
        GGC,
        GGG,
        GGT,
        GTA,
        GTC,
        GTG,
        GTT,
        TAA,
        TAC,
        TAG,
        TAT,
        TCA,
        TCC,
        TCG,
        TCT,
        TGA,
        TGC,
        TGG,
        TGT,
        TTA,
        TTC,
        TTG,
        TTT
    }

    /**
     * List of statistics (float)
     */
    public enum StatFloat {
        FREQ0,
        FREQ1,
        FREQ2
    }

    /**
     * List of statistics (long)
     */
    public enum StatLong {
        PHASE0,
        PHASE1,
        PHASE2,
        PREF0,
        PREF1,
        PREF2
    }

    /**
     * This enumeration represent the type of a Statistic
     */
    public enum Type {
        CHROMOSOME,
        MITOCHONDRION,
        PLASMID,
        DNA,
        CHLOROPLAST
    }
}
