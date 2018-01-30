package Data;

import java.util.ArrayList;
import java.util.EnumMap;

public class Replicon extends IDataBase{
	
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
		PHASE2
	}
	
	/**
	 * Type of this Replicon
	 */
	private Type m_type;
	/**
	 * Array of all the sequences of this Replicon
	 */
	private ArrayList<StringBuffer> m_sequences;
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
	 * @param _type, the type of this Replicon
	 */
	public Replicon(Type _type, String _name) {
		super(_name);
		m_type = _type;
		m_sequences = new ArrayList<>();
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
	 * Add a sequence 
	 * @param _sequence, the sequence to add
	 * @return the insertion success
	 */
	public boolean addSequence(StringBuffer _sequence) {
		return m_sequences.add(_sequence);
	}
	
	/**
	 * Set a value to a statistic of a Trinucleotide
	 * @param _tri, the Trinucleotide to set
	 * @param _stat, the statistic to set
	 * @param _val, the value to set
	 * @return the element previously at the specified position
	 */
	public Float setStat(Trinucleotide _tri, Stat _stat, Float _val) {
		return m_statitics.get(_tri).put(_stat, _val);
	}
	
	/**
	 * Get the statistic of a trinucleotide  
	 * @param _tri, the trinucleotide to get
	 * @param _stat, the statistic to get
	 * @return the statistic
	 */
	public Float getStat(Trinucleotide _tri, Stat _stat) {
		return m_statitics.get(_tri).get(_stat);
	}
	
	/**
	 * Get the type of this Replicon
	 * @return the type
	 */
	public Type getType() {
		return m_type;
	}
	
	/**
	 * Get sequences of this Replicon
	 * @return the sequences
	 */
	public ArrayList<StringBuffer> getSequences(){
		return m_sequences;
	}
	
	/**
	 * Get statistic of this NC
	 * @return the statistic
	 */
	public EnumMap<Trinucleotide, EnumMap<Stat, Float>> getStat(){
		return m_statitics;
	}
	
	/**
	 * @return the m_validSequence
	 */
	public long getValidSequence() {
		return m_validSequence;
	}

	/**
	 * @return the m_invalidSequences
	 */
	public long getInvalidSequences() {
		return m_invalidSequences;
	}
	
}
