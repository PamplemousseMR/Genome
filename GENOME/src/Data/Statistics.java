package Data;

import java.io.Serializable;
import java.util.stream.IntStream;

public class Statistics implements Serializable {

    /**
     * Type of this Statistic
     */
    private final Type m_type;
    /**
     * Array to store statistics
     */
    private final Tuple[] m_trinucleotideTable;
    /**
     * Number total of trinucleotide on phase 0
     */
    private long m_totalTrinucleotide;
    /**
     * The number of CDS sequences
     */
    private long m_CDSNumber;
    /**
     * The number of valid CDS sequences
     */
    private long m_validCDSNumber;

    /**
     * Class constructor
     */
    Statistics(Type _type) {
        m_type = _type;
        m_trinucleotideTable = new Tuple[Trinucleotide.values().length];
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> m_trinucleotideTable[i] = new Tuple());
        m_totalTrinucleotide = 0L;
        m_CDSNumber = 0L;
        m_validCDSNumber = 0L;
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
    public final Tuple[] getTable() {
        return m_trinucleotideTable;
    }

    /**
     * Get the number of CDS
     *
     * @return the number of CDS
     */
    public final long getCDSNumber() {
        return m_CDSNumber;
    }

    /**
     * Get the number of valid CDS
     *
     * @return the number of valid CDS
     */
    public final long getValidCDSNumber() {
        return m_validCDSNumber;
    }

    /**
     * Update statistics
     *
     * @param _stats, the stats use to update
     */
    protected final void update(Statistics _stats) {
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final Trinucleotide tri = Trinucleotide.values()[i];
            final Tuple inputRow = _stats.m_trinucleotideTable[i];
            incrementStat(tri, StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            incrementStat(tri, StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            incrementStat(tri, StatLong.PHASE2, inputRow.get(StatLong.PHASE2));
            incrementStat(tri, StatLong.PREF0, inputRow.get(StatLong.PREF0));
            incrementStat(tri, StatLong.PREF1, inputRow.get(StatLong.PREF1));
            incrementStat(tri, StatLong.PREF2, inputRow.get(StatLong.PREF2));
        });
        m_totalTrinucleotide += _stats.m_totalTrinucleotide;
        m_CDSNumber += _stats.m_CDSNumber;
        m_validCDSNumber += _stats.m_validCDSNumber;
    }

    /**
     * Compute the frequencies and the preferences of each trinucleotide for each phases
     */
    protected final void compute() {
        if (m_totalTrinucleotide != 0) {
            for (Tuple row : m_trinucleotideTable) {
                row.set(StatFloat.FREQ0, row.get(StatLong.PHASE0) / (float) m_totalTrinucleotide);
                row.set(StatFloat.FREQ1, row.get(StatLong.PHASE1) / (float) m_totalTrinucleotide);
                row.set(StatFloat.FREQ2, row.get(StatLong.PHASE2) / (float) m_totalTrinucleotide);
            }
        }
    }

    /**
     * Increment by 1 the value of a trinucleotide for a stat
     *
     * @param _tri,  the Trinucleotide to set
     * @param _stat, the statistic to set
     */
    protected final void incrementStat(Trinucleotide _tri, StatLong _stat) {
        m_trinucleotideTable[_tri.ordinal()].incr(_stat, 1L);
    }

    /**
     * Increment the value of total trinucleotide by the parameter
     *
     * @param _inc, the value to increment
     */
    protected final void incrementTotal(long _inc) {
        m_totalTrinucleotide += _inc;
    }

    /**
     * Increment the number of CDS sequence
     *
     * @param _long, the value to increment
     */
    protected final void incrementCDS(long _long) {
        m_CDSNumber += _long;
    }

    /**
     * Increment the number of valid CDS sequence
     *
     * @param _long, the value to increment
     */
    protected final void incrementValidCDS(long _long) {
        m_validCDSNumber += _long;
    }

    /**
     * Increment the value of a trinucleotide for a stat by the parameter
     *
     * @param _tri,  the Trinucleotide to set
     * @param _stat, the statistic to set
     * @param _inc,  the value to increment
     */
    private void incrementStat(Trinucleotide _tri, StatLong _stat, long _inc) {
        m_trinucleotideTable[_tri.ordinal()].incr(_stat, _inc);
    }

    /**
     * Increment the value of a trinucleotide for a stat by the parameter
     *
     * @param _tri,  the Trinucleotide to set
     * @param _stat, the statistic to set
     * @param _inc,  the value to increment
     */
    private void decrementStat(Trinucleotide _tri, StatLong _stat, long _inc) {
        m_trinucleotideTable[_tri.ordinal()].decr(_stat, _inc);
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
        CHLOROPLAST,
        SEGMENT,
        LINKAGE,
        PLASTID,
        CIRCLE,
        RNA,
        PLTD,
        UNKNOWN,
        UNNAMED;

        public static Type isTypeOf(String _s) {
            if (_s.contains("CHLOROPLAST")) {
                return CHLOROPLAST;
            } else if (_s.contains("CHROMOSOME")) {
                return CHROMOSOME;
            } else if (_s.contains("PLASMID")) {
                return PLASMID;
            } else if (_s.contains("SEGMENT") || _s.contains("SEGMEN")) {
                return SEGMENT;
            } else if (_s.contains("LINKAGE")) {
                return LINKAGE;
            } else if (_s.contains("PLASTID")) {
                return PLASTID;
            } else if (_s.contains("CIRCLE")) {
                return CIRCLE;
            } else if (_s.contains("PLTD")) {
                return PLTD;
            } else if (_s.contains("UNKNOWN")) {
                return UNKNOWN;
            } else if (_s.contains("UNNAMED")) {
                return UNNAMED;
            } else if (_s.contains("MITOCHONDRION") || _s.contains("MT")) {
                return MITOCHONDRION;
            } else if (_s.contains("DNA") || _s.contains("DN")) {
                return DNA;
            } else if (_s.contains("RNA") || _s.contains("RN")) {
                return RNA;
            }
            return null;
        }
    }

    protected void unload(Statistics _stat){
        m_totalTrinucleotide -= _stat.m_totalTrinucleotide;
        m_CDSNumber -= _stat.m_CDSNumber;
        m_validCDSNumber -= _stat.m_validCDSNumber;
        IntStream.range(0, Trinucleotide.values().length).parallel().forEach(i -> {
            final Trinucleotide tri = Trinucleotide.values()[i];
            final Tuple inputRow = _stat.m_trinucleotideTable[i];
            decrementStat(tri, StatLong.PHASE0, inputRow.get(StatLong.PHASE0));
            decrementStat(tri, StatLong.PHASE1, inputRow.get(StatLong.PHASE1));
            decrementStat(tri, StatLong.PHASE2, inputRow.get(StatLong.PHASE2));
            decrementStat(tri, StatLong.PREF0, inputRow.get(StatLong.PREF0));
            decrementStat(tri, StatLong.PREF1, inputRow.get(StatLong.PREF1));
            decrementStat(tri, StatLong.PREF2, inputRow.get(StatLong.PREF2));
        });

    }
}
