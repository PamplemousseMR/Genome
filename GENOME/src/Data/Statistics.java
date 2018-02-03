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
     * This enumeration represent the type of a Replicon
     */
    public enum Type{
        CHROMOSOME,
        MITOCHONDRION,
        PLASMID,
        DNA,
        CHLOROPLAST
    }

    /**
     * Type of this Replicon
     */
    private Type m_type;
    /**
     * Array to store statistics
     */
    private EnumMap<Trinucleotide, EnumMap<Stat,Float>> m_trinucleaotideTable;
    /**
     * Number of valid sequence
     */
    private long m_validSequence;
    /**
     * Number of invalid sequence
     */
    private long m_invalidSequences;
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
    public Statistics(Type _type){
        m_type = _type;
        m_trinucleaotideTable = new EnumMap<>(Trinucleotide.class);
        for(Trinucleotide tri : Trinucleotide.values()) {
            EnumMap<Stat,Float> arr = new EnumMap<>(Stat.class);
            for(Stat stat :  Stat.values()) {
                arr.put(stat, 0f);
            }
            m_trinucleaotideTable.put(tri,arr);
        }
        m_validSequence = 0;
        m_invalidSequences = 0;
        m_TotalTriPhase0 = 0;
        m_TotalTriPhase1 = 0;
        m_TotalTriPhase2 = 0;
    }

    /**
     * Set a value to a statistic of a Trinucleotide
     * @param _tri, the Trinucleotide to set
     * @param _stat, the statistic to set
     * @param _val, the value to set
     * @return the element previously at the specified position
     */
    protected Float setStat(Trinucleotide _tri, Stat _stat, Float _val) {
        return m_trinucleaotideTable.get(_tri).put(_stat, _val);
    }

    /**
     * Increment the value of a trinucleotide for a stat
     * @param _tri, the Trinucleotide to set
     * @param _stat, the statistic to set
     */
    protected void incrStat(Trinucleotide _tri, Stat _stat){
        m_trinucleaotideTable.get(_tri).put(_stat,m_trinucleaotideTable.get(_tri).get(_stat));
    }

    /**
     * Get the statistic of a trinucleotide
     * @param _tri, the trinucleotide to get
     * @param _stat, the statistic to get
     * @return the statistic
     */
    public Float getStat(Trinucleotide _tri, Stat _stat) {
        return m_trinucleaotideTable.get(_tri).get(_stat);
    }

    /**
     * Get the type of this Replicon
     * @return the type
     */
    public Type getType() {
        return m_type;
    }

    /**
     * Get the valid sequences number
     * @return the m_validSequence
     */
    public long getValidSequence() {
        return m_validSequence;
    }

    /**
     * Get the invalid sequences number
     * @return the m_invalidSequences
     */
    public long getInvalidSequences() {
        return m_invalidSequences;
    }

    /**
     * get the total trinucleotide of the phase 0 number
     * @return the m_TotalTriPhase0
     */
    public long getTotalTinucleotidePhase0() { return m_TotalTriPhase0; }

    /**
     * get the total trinucleotide of the phase 1 number
     * @return the m_TotalTriPhase1
     */
    public long getTotalTinucleotidePhase1() { return m_TotalTriPhase1; }

    /**
     * get the total trinucleotide of the phase 2 number
     * @return the m_TotalTriPhase2
     */
    public long getTotalTinucleotidePhase2() { return m_TotalTriPhase2; }

    /**
     * Update statistics
     * @param _stats, the stats use to update
     */
    void update(Statistics _stats) {
        EnumMap row, inputRow;
        for (Trinucleotide tri : Trinucleotide.values()) {
            row = m_trinucleaotideTable.get(tri);
            inputRow = _stats.m_trinucleaotideTable.get(tri);
            row.put(Stat.PHASE0, (Float)row.get(Stat.PHASE0)+(Float)inputRow.get(Stat.PHASE0));
            row.put(Stat.PHASE1, (Float)row.get(Stat.PHASE1)+(Float)inputRow.get(Stat.PHASE1));
            row.put(Stat.PHASE2, (Float)row.get(Stat.PHASE2)+(Float)inputRow.get(Stat.PHASE2));
        }
    }

    /**
     * Compute the frequencies and the preferences of each trinucleotide for each phases
     */
    void compute(){
        EnumMap row;
        for(Trinucleotide tri : Trinucleotide.values()){
            row = m_trinucleaotideTable.get(tri);
            row.put(Stat.FREQ0, (Float)row.get(Stat.PHASE0)/(float)m_TotalTriPhase0);
            row.put(Stat.FREQ0, (Float)row.get(Stat.PHASE1)/(float)m_TotalTriPhase1);
            row.put(Stat.FREQ0, (Float)row.get(Stat.PHASE2)/(float)m_TotalTriPhase2);
        }
    }
}
