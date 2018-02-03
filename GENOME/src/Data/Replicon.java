package Data;

import java.util.Date;
import java.util.LinkedList;

public final class Replicon extends Statistics {

	/**
	 * The name
	 */
	private String m_name;
	/**
	 * Last modification's date
	 */
	private Date m_modificationDate;
	/**
	 * Array of all the sequences of this Replicon
	 */
	private LinkedList<StringBuffer> m_sequences;

	/**
	 * Class constructor
	 * @param _type, the type of this Replicon
     * @param _name, the name of the organism
     */
	public Replicon(Type _type, String _name) {
		super(_type);
		m_name = _name;
		m_modificationDate = new Date();
		m_sequences = new LinkedList<>();
	}

	/**
	 * Get the last modification's date
	 * @return the m_modificationDate
	 */
	public Date getModificationDate() {
		return m_modificationDate;
	}

	/**
	 * Get the name
	 * @return the m_name
	 */
	public String getName(){
		return m_name;
	}
	
	/**
	 * Add a sequence 
	 * @param _sequence, the sequence to add
	 * @return the insertion success
	 * @throws Exception if the _sequence are already added
	 */
	public boolean addSequence(StringBuffer _sequence) throws Exception {
		if(m_sequences.contains(_sequence))
			throw new Exception("Sequence already added");
		return m_sequences.add(_sequence);
	}

	// Do not use

	protected void computeStatistic() {
		int idx,length;
		for( StringBuffer sequence : m_sequences) {
				idx = 0;
				length = sequence.length();
				while(idx+5 <= length){
					incrementStat(Trinucleotide.valueOf(sequence.substring(idx,idx+3)),Stat.PHASE0);
					incrementStat(Trinucleotide.valueOf(sequence.substring(idx+1,idx+4)),Stat.PHASE1);
					incrementStat(Trinucleotide.valueOf(sequence.substring(idx+2,idx+5)),Stat.PHASE2);
                    idx+=3;
				}
				if(idx+4 <= length){
					incrementStat(Trinucleotide.valueOf(sequence.substring(idx,idx+3)),Stat.PHASE0);
                    if(idx+4 == length){
						incrementStat(Trinucleotide.valueOf(sequence.substring(idx+1,idx+4)),Stat.PHASE1);
                    }
                }
		}
		super.compute();
	}
}
