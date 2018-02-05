package Data;

import java.util.EnumMap;
import java.util.stream.IntStream;

public class Statistics {

    /**
     * List of the 64 trinucleotide
     */
    public enum Trinucleotide{
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
     * List of statistics
     */
    public enum Stat{
        PHASE0,
        PHASE1,
        PHASE2,
        FREQ0,
        FREQ1,
        FREQ2,
        PREF0,
        PREF1,
        PREF2
    }

    /**
     * This enumeration represent the type of a Statistic
     */
    public enum Type{
        CHROMOSOME,
        MITOCHONDRION,
        PLASMID,
        DNA,
        CHLOROPLAST
    }

    /**
     * Type of this Statistic
     */
    private Type m_type;
    /**
     * Array to store statistics
     */
    private EnumMap<Trinucleotide, EnumMap<Stat,Float>> m_trinucleotideTable;
    /**
     * Number total of trinucleotide on phase 0
     */
    protected long m_totalTrinucleotide;

    /**
     * Class constructor
     */
    protected Statistics(Type _type){
        m_type = _type;
        m_trinucleotideTable = new EnumMap<>(Trinucleotide.class);
        IntStream.range(0,Trinucleotide.values().length).forEach(i -> {
            EnumMap<Stat,Float> arr = new EnumMap<>(Stat.class);
            for(Stat stat :  Stat.values()) {
                arr.put(stat, 0f);
            }
            m_trinucleotideTable.put(Trinucleotide.values()[i],arr);
        });
        m_totalTrinucleotide = 0;
    }

    /**
     * Get the type of this Replicon
     * @return the type
     */
    public Type getType() {
        return m_type;
    }

    /**
     * get the total trinucleotide of the phase 0 number
     * @return the m_TotalTriPhase0
     */
    public long getTotalTrinucleotide() { return m_totalTrinucleotide; }


    /**
     * 
     * @return the m_trinucleotideTable
     */
    public EnumMap<Trinucleotide, EnumMap<Stat, Float>> getTable() {
        return m_trinucleotideTable;
    }

    // Do not use

    /**
     * Update statistics
     * @param _stats, the stats use to update
     */
    protected void update(Statistics _stats) {
        for (Trinucleotide tri : Trinucleotide.values()) {
            EnumMap inputRow = _stats.m_trinucleotideTable.get(tri);
            incrementStat(tri, Stat.PHASE0,(Float)inputRow.get(Stat.PHASE0));
            incrementStat(tri, Stat.PHASE1,(Float)inputRow.get(Stat.PHASE1));
            incrementStat(tri, Stat.PHASE2,(Float)inputRow.get(Stat.PHASE2));
        }
        m_totalTrinucleotide += _stats.m_totalTrinucleotide;
    }

    /**
     * Compute the frequencies and the preferences of each trinucleotide for each phases
     */
    protected void compute(){
        m_trinucleotideTable.values().parallelStream().forEach(row -> {
            row.put(Stat.FREQ0, row.get(Stat.PHASE0) / (float) m_totalTrinucleotide);
            row.put(Stat.FREQ1, row.get(Stat.PHASE1) / (float) m_totalTrinucleotide);
            row.put(Stat.FREQ2, row.get(Stat.PHASE2) / (float) m_totalTrinucleotide);
        });
    }

    /**
     * Increment by 1 the value of a trinucleotide for a stat
     * @param _tri, the Trinucleotide to set
     * @param _stat, the statistic to set
     */
    protected void incrementStat(Trinucleotide _tri, Stat _stat){
        m_trinucleotideTable.get(_tri).put(_stat,m_trinucleotideTable.get(_tri).get(_stat)+1);
    }

    /**
     * Increment by _incr the value of a trinucleotide for a stat
     * @param _tri, the Trinucleotide to set
     * @param _stat, the statistic to set
     * @param _incr, the value to increment
     */
    private void incrementStat(Trinucleotide _tri, Stat _stat, float _incr){
        m_trinucleotideTable.get(_tri).put(_stat,m_trinucleotideTable.get(_tri).get(_stat)+_incr);
    }
}
