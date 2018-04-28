# Application cree par -- Romain M. -- Florian H. -- Vincent H. -- Sami F. -- Arthur D.  -- Romain T. -- Adele M.

# Configuration:

- Un fichier option.ini qui permet a l'utilisateur de regler un certain nombre de parametres:
	- s_SAVE_GENE=false : mettre a true pour sauvegarder les genes des organismes
	- s_DOWNLOAD_STEP_ORGANISM=100000 : Valeur du pas de telechargement des organismes
	- s_SERIALIZE_DIRECTORY=Save : Nom du repertoire de serialisation pour java
	- s_TOTAL_PREFIX=Total_ : Prefixe des fichiers excel pour une famille (sous-groupe ou groupe ou kingdom)
	- s_EXCEL_EXTENSION=.xlsx : Extension des fichiers de statistiques
	- s_GENBANK_NAME=Genbank : Nom de la racine de l'arborescence pour la sauvegarde
	- s_KINGDOM_SERIALIZATION_PREFIX=K_ : Prefixe permettant de faire du parsing plus rapidement (indique le prefixe du kingdom)
	- s_SUBGROUP_SERIALIZATION_PREFIX=SG_ : Prefixe permettant de faire du parsing plus rapidement (indique le prefixe du sous-groupe)
	- s_DATEMODIF_SERIALIZE_EXTENSION=--DATEMODIF--.ser : Extension pour les fichiers de serialisation des dates de mises a jour des organismes
	- s_MAX_THREAD=32 : Nombre maximum de thread a utiliser (ne depassera jamais 4 fois le nombre de coeur de la machine, quelque soit la valeur indiquee)
	- s_GENE_EXTENSION=.txt : Extension des fichiers de sauvegarde des genes des organismes
	- s_GROUP_SERIALIZATION_PREFIX=G_ : Prefixe permettant de faire du parsing plus rapidement (indique le prefixe du groupe)
	- s_SAVE_GENOME=false : A mettre a true pour sauvegarder le genome des organismes
	- s_GENOME_DIRECTORY=Genomes : Nom du repertoire de sauvegarde des genomes
	- s_CDS_BASE_URL=https\://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi : URL de base pour telecharger un CDS
	- s_GENE_DIRECTORY=Genes : Nom du repertoire de sauvegarde des genes des organismes
	- s_DOWNLOAD_CONNECTION_TIMEOUT=30000 : Timeout pour telechargement
	- s_RESULT_DIRECTORY=Results : Nom du repertoire de resultat des fichiers excel
	- s_DATABASE_SERIALIZATION_PREFIX=D_ : Prefixe permettant de faire du parsing plus rapidement (indique le prefixe de la base de donnees)
	- s_ORGANISM_BASE_URL=https\://www.ncbi.nlm.nih.gov/Structure/ngram : URL pour telecharger la liste des organismes presents sur GenBank
	- s_SERIALIZATION_SPLITER=-- : Spliter pour parser plus rapidement la serialisation
	- s_SUM_PREFIX=Sum_ : Prefixe utilise pour els sommes dans les fichiers excel
	- s_MUTEX_FILE_NAME=locker.mutex : Nom du fichier de verrou de l'application pour empêcher plusieurs executions simultanees
	- s_GENOME_EXTENSION=.txt : Extension des fichiers de genome
	- s_ORGANISM_SERIALIZATION_PREFIX=O_ : Prefixe permettant de faire du parsing plus rapidement (indique le prefixe des organismes)
	- s_SERIALIZE_EXTENSION=--.ser : Extension des fichiers de serialisation

- Ce fichier est lu au debut de l'execution et est utilise pour toute la duree de vie de programme. S'il n'existe pas (s'il est supprime par erreur), il est regenere a l'arrêt de l'application avec les valeurs par defaut.

# Fichiers et repertoires utilises par le programme
	
- Results : repertoire des fichiers excel
- Save: repertoire des fichiers de serialisation
- options.ini : fichier des options configurables par l'utilisateur
- log.txt : fichier contenant les logs complets generes lors de l'execution
- locker.mutex : Fichier de verrou de l'application pour empêcher plusieurs executions simultanees. Une fenêtre speciale s'affiche en cas de tentative.

# Affichage de l'interface: 

- L'arbre: Affiche en temps reel l'arborescence des fichiers avec un code couleur 
	- blanc: fichiers/dossiers actuellement enregistres
	- orange: fichier/dossiers en cours d'actualisation
	- bleu: fichiers/dossiers qui sont en cours de telechargement
	- vert: fichiers/dossiers dont le telechargement et les mises a jour sont termines
- Une barre de progression qui montre l'avancement du telechargement en fonction du nombre d'organisme. Le total ne comptabilise pas les doublons et les organismes qui ne possedent pas de replicons de type NC_.
- Les logs: Affichage des principaux logs pour l'utilisateur
- Les informations: Affichage des donnees pour un organisme ou une famille selectionne dans l'arbre

# Utilisation de l'interface:

- Un bouton "demarrer le telechargement". Une fois lance, devient un bouton de pause.
- Un bouton "pause" qui envoie une requête de pause a tous les threads de telechargement/calcul du programme pour les mettre en pause. Ce bouton devient ensuite un bouton de relance
- Un bouton "relance" pour reveiller les threads en pause et reprendre le deroulement de l'application.
- Un bouton "stop" pour stopper proprement a tout moment l'execution du programme. Envoie une requête d'arrêt a tous les threads de telechargement/calcul du programme. Le programme attend la terminaison des actions en cours pour garantir un etat stable des fichiers.
