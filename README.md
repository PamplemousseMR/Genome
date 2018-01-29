# BioInfo

BIOINFORMATIQUE ILC
Projet réalisé par:

Réalisé par:
-- Arthur D.
-- Adèle M.
-- Florian H.
-- Romain M.
-- Romain T.
-- Sami F.
-- Vincent H.;

Conventions de nommage:

Variable membre ou attribut de classe static : commencent par "s_"
Variable membre ou attribut de classe non static  "m_"
Variable final : partie à droite du "_" en majuscule
Méthode en anglais minuscule premier mot et majuscule second mot. ex: setPuissance

Conventions de commit:

- feat
- refractor 		//si recommit
- fix
- style			// si modification de doc ou swag

Mot clé suivi des fichiers ou des packages impacté (entre parenthèses)

AUCUN WARNING

Exemples d'utilisation de la classe Organism :

Organism or = new Organism("Ananas");
{
	NC nc1 = new NC(Type.CHROMOSOME,"nc1");

	nc1.setStat(Trinucleotide.AAA,Stat.PHASE0,new Float(12));
	
	or.addNC(nc1);
	System.out.println(or.getName() + " " + or.getTypeNumber(Type.CHROMOSOME));
	
	NC nc2 = new NC(Type.CHLOROPLAST,"nc2");
	or.addNC(nc2);
	System.out.println(or.getName() + " " + or.getTypeNumber(Type.CHROMOSOME));
	
	NC nc3 = new NC(Type.CHROMOSOME,"nc2");
	or.addNC(nc3);
	System.out.println(or.getName() + " " + or.getTypeNumber(Type.CHROMOSOME));
}

{
	for(NC nc : or.getNcs()) {
		System.out.println(nc.getName() + " " + nc.getStat(Trinucleotide.AAA,Stat.PHASE0));
	}
}		

Complexité des contenaires en java

List                 | Add  | Remove | Get  | Contains | Next | Data Structure
---------------------|------|--------|------|----------|------|---------------
ArrayList            | O(1) |  O(n)  | O(1) |   O(n)   | O(1) | Array
LinkedList           | O(1) |  O(1)  | O(n) |   O(n)   | O(1) | Linked List
CopyOnWriteArrayList | O(n) |  O(n)  | O(1) |   O(n)   | O(1) | Array



Set                   |    Add   |  Remove  | Contains |   Next   | Size | Data Structure
----------------------|----------|----------|----------|----------|------|-------------------------
HashSet               | O(1)     | O(1)     | O(1)     | O(h/n)   | O(1) | Hash Table
LinkedHashSet         | O(1)     | O(1)     | O(1)     | O(1)     | O(1) | Hash Table + Linked List
EnumSet               | O(1)     | O(1)     | O(1)     | O(1)     | O(1) | Bit Vector
TreeSet               | O(log n) | O(log n) | O(log n) | O(log n) | O(1) | Red-black tree
CopyOnWriteArraySet   | O(n)     | O(n)     | O(n)     | O(1)     | O(1) | Array
ConcurrentSkipListSet | O(log n) | O(log n) | O(log n) | O(1)     | O(n) | Skip List



Queue                   |  Offer   | Peak |   Poll   | Remove | Size | Data Structure
------------------------|----------|------|----------|--------|------|---------------
PriorityQueue           | O(log n) | O(1) | O(log n) |  O(n)  | O(1) | Priority Heap
LinkedList              | O(1)     | O(1) | O(1)     |  O(1)  | O(1) | Array
ArrayDequeue            | O(1)     | O(1) | O(1)     |  O(n)  | O(1) | Linked List
ConcurrentLinkedQueue   | O(1)     | O(1) | O(1)     |  O(n)  | O(n) | Linked List
ArrayBlockingQueue      | O(1)     | O(1) | O(1)     |  O(n)  | O(1) | Array
PriorirityBlockingQueue | O(log n) | O(1) | O(log n) |  O(n)  | O(1) | Priority Heap
SynchronousQueue        | O(1)     | O(1) | O(1)     |  O(n)  | O(1) | None!
DelayQueue              | O(log n) | O(1) | O(log n) |  O(n)  | O(1) | Priority Heap
LinkedBlockingQueue     | O(1)     | O(1) | O(1)     |  O(n)  | O(1) | Linked List



Map                   |   Get    | ContainsKey |   Next   | Data Structure
----------------------|----------|-------------|----------|-------------------------
HashMap               | O(1)     |   O(1)      | O(h / n) | Hash Table
LinkedHashMap         | O(1)     |   O(1)      | O(1)     | Hash Table + Linked List
IdentityHashMap       | O(1)     |   O(1)      | O(h / n) | Array
WeakHashMap           | O(1)     |   O(1)      | O(h / n) | Hash Table
EnumMap               | O(1)     |   O(1)      | O(1)     | Array
TreeMap               | O(log n) |   O(log n)  | O(log n) | Red-black tree
ConcurrentHashMap     | O(1)     |   O(1)      | O(h / n) | Hash Tables
ConcurrentSkipListMap | O(log n) |   O(log n)  | O(1)     | Skip List