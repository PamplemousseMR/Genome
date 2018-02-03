package Data;

import java.util.LinkedList;

public final class Replicon extends IDataBase{
	
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
	 * Array of all the sequences of this Replicon
	 */
	private LinkedList<StringBuffer> m_sequences;
	
	/**
	 * Class constructor
	 * @param _type, the type of this Replicon
     * @param _name, the name of the organism
     */
	public Replicon(Type _type, String _name) {
		super(_name);
		m_type = _type;
		getStatistics().setType(_type);
		m_sequences = new LinkedList<>();
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
	public LinkedList<StringBuffer> getSequences(){
		return m_sequences;
	}

}
