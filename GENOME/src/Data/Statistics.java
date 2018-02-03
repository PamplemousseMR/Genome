package Data;

import java.util.EnumMap;

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
    private long m_TotalTriPhase0;
    /**
     * Number total of trinucleotide on phase 1
     */
    private long m_TotalTriPhase1;
    /**
     * Number total of trinucleotide on phase 2
     */
    private long m_TotalTriPhase2;

    /**
     * Class constructor
     */
    protected Statistics(Type _type){
        m_type = _type;
        m_trinucleotideTable = new EnumMap<>(Trinucleotide.class);
        for(Trinucleotide tri : Trinucleotide.values()) {
            EnumMap<Stat,Float> arr = new EnumMap<>(Stat.class);
            for(Stat stat :  Stat.values()) {
                arr.put(stat, 0f);
            }
            m_trinucleotideTable.put(tri,arr);
        }
        m_TotalTriPhase0 = 0;
        m_TotalTriPhase1 = 0;
        m_TotalTriPhase2 = 0;
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
    public long getTotalTrinucleotidePhase0() { return m_TotalTriPhase0; }

    /**
     * get the total trinucleotide of the phase 1 number
     * @return the m_TotalTriPhase1
     */
    public long getTotalTrinucleotidePhase1() { return m_TotalTriPhase1; }

    /**
     * get the total trinucleotide of the phase 2 number
     * @return the m_TotalTriPhase2
     */
    public long getTotalTrinucleotidePhase2() { return m_TotalTriPhase2; }

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
        EnumMap row, inputRow;
        for (Trinucleotide tri : Trinucleotide.values()) {
            row = m_trinucleotideTable.get(tri);
            inputRow = _stats.m_trinucleotideTable.get(tri);
            incrementStat(tri, Stat.PHASE0,(Float)inputRow.get(Stat.PHASE0));
            incrementStat(tri, Stat.PHASE1,(Float)inputRow.get(Stat.PHASE1));
            incrementStat(tri, Stat.PHASE2,(Float)inputRow.get(Stat.PHASE2));
        }
    }

    /**
     * Compute the frequencies and the preferences of each trinucleotide for each phases
     */
    protected void compute(){
        EnumMap row;
        for(Trinucleotide tri : Trinucleotide.values()){
            row = m_trinucleotideTable.get(tri);
            row.put(Stat.FREQ0, (Float)row.get(Stat.PHASE0)/(float)m_TotalTriPhase0);
            row.put(Stat.FREQ1, (Float)row.get(Stat.PHASE1)/(float)m_TotalTriPhase1);
            row.put(Stat.FREQ2, (Float)row.get(Stat.PHASE2)/(float)m_TotalTriPhase2);
        }
    }

    /**
     * Increment by 1 the value of a trinucleotide for a stat
     * @param _tri, the Trinucleotide to set
     * @param _stat, the statistic to set
     */
    protected void incrementStat(Trinucleotide _tri, Stat _stat){
        m_trinucleotideTable.get(_tri).put(_stat,m_trinucleotideTable.get(_tri).get(_stat)+1);
        switch(_stat){
            case PHASE0: m_TotalTriPhase0++; break;
            case PHASE1: m_TotalTriPhase1++; break;
            case PHASE2: m_TotalTriPhase2++; break;
        }
    }

    /**
     * Increment by _incr the value of a trinucleotide for a stat
     * @param _tri, the Trinucleotide to set
     * @param _stat, the statistic to set
     * @param _incr, the value to increment
     */
    private void incrementStat(Trinucleotide _tri, Stat _stat, float _incr){
        m_trinucleotideTable.get(_tri).put(_stat,m_trinucleotideTable.get(_tri).get(_stat)+_incr);
        switch(_stat){
            case PHASE0: m_TotalTriPhase0+=_incr; break;
            case PHASE1: m_TotalTriPhase1+=_incr; break;
            case PHASE2: m_TotalTriPhase2+=_incr; break;
        }
    }
}
