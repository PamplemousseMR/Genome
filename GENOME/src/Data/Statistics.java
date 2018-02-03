package Data;

import java.util.EnumMap;

public final class Statistics {

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
     * Array of values of each Replicon's type
     */
    private EnumMap<Replicon.Type,Long> m_genomeNumber;
    /**
     * Array to store statistics
     */
    private EnumMap<Trinucleotide, EnumMap<Stat,Float>> m_statitics;
    /**
     * Number of valid sequence
     */
    private long m_validSequence;
    /**
     * Number of invalid sequence
     */
    private long m_invalidSequences;

    /**
     * Class constructor
     */
    Statistics(){
        m_genomeNumber = new EnumMap<>(Replicon.Type.class);
        for(Replicon.Type field : Replicon.Type.values()) {
            m_genomeNumber.put(field, 0L);
        }
        m_statitics = new EnumMap<>(Trinucleotide.class);
        for(Trinucleotide tri : Trinucleotide.values()) {
            EnumMap<Stat,Float> arr = new EnumMap<>(Stat.class);
            for(Stat stat :  Stat.values()) {
                arr.put(stat, 0f);
            }
            m_statitics.put(tri,arr);
        }
        m_validSequence = 0;
        m_invalidSequences = 0;
    }

    /**
     * Get the number of a genome's specified type
     * @param _type, the Type of the genomes's number to get
     * @return the number of genomes
     */
    Long getTypeNumber(Replicon.Type _type) {
        return m_genomeNumber.get(_type);
    }

    /**
     * Set 1 to the specified type
     * @param _type, the Type of the genomes's number to get
     */
    void setType(Replicon.Type _type) {
        m_genomeNumber.put(_type,1L);
    }

    /**
     * Set a value to a statistic of a Trinucleotide
     * @param _tri, the Trinucleotide to set
     * @param _stat, the statistic to set
     * @param _val, the value to set
     * @return the element previously at the specified position
     */
    Float setStat(Trinucleotide _tri, Stat _stat, Float _val) {
        return m_statitics.get(_tri).put(_stat, _val);
    }

    /**
     * Get the statistic of a trinucleotide
     * @param _tri, the trinucleotide to get
     * @param _stat, the statistic to get
     * @return the statistic
     */
    Float getStat(Trinucleotide _tri, Stat _stat) {
        return m_statitics.get(_tri).get(_stat);
    }

    /**
     * Get the valid sequences number
     * @return the m_validSequence
     */
    long getValidSequence() {
        return m_validSequence;
    }

    /**
     * Get the invalid sequences number
     * @return the m_invalidSequences
     */
    long getInvalidSequences() {
        return m_invalidSequences;
    }

    /**
     * Update statistics
     * @param _stats, the stats use to update
     */
    void update(Statistics _stats){
        for(Replicon.Type field : Replicon.Type.values()) {
            m_genomeNumber.put(field,m_genomeNumber.get(field) + _stats.getTypeNumber(field));
        }
    }
}
