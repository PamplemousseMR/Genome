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
     * @param _parent, the reference to the parent
	 * @param _type, the type of this Replicon
     * @param _name, the name of the organism
     */
	public Replicon(Organism _parent, Type _type, String _name) {
		super(_name);
		m_statistics.setTypeNumber(_type,1l);
        m_parent = _parent;
		m_type = _type;
		m_sequences = new LinkedList<>();
		m_parent.addReplicon(this);
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

    /**
     * Update m_parent and remove this from the parent's list
     */
	public void update(){
	    m_parent.getReplicons().remove(this);
	    m_parent.update(m_statistics);
    }
}
