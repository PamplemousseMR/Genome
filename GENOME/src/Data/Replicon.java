package Data;

import java.util.LinkedList;

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
     * Reference to the parent
     */
    private Organism m_parent;
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
		getStatistics().setTypeNumber(_type,1l);
		m_sequences = new LinkedList<>();
		m_parent = null;
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

	// Do not use

	/**
	 * Set the parent
	 * @param _organism, the parent to set
	 */
	protected void setParent(Organism _organism){
		m_parent = _organism;
	}

}
