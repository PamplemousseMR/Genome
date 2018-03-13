package Data.Tests;

import Data.*;
import Exception.AddException;
import Exception.InvalidStateException;
import Utils.Logs;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializableTest {

    private static void printTriTable(Statistics _stat) {
        System.out.print("TRI\tPhase0\tFreq0\tPhase1\tFreq1\tPhase2\tFreq2\tPref0\tPref1\tPref2\t");
        for (Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()) {
            Tuple row = _stat.getTable()[tri.ordinal()];
            System.out.print("\n" + tri + "\t");
            if(row != null) {
                System.out.print(row.get(Statistics.StatLong.PHASE0) + "\t");
                System.out.print(String.format("%.4f\t", row.get(Statistics.StatFloat.FREQ0)));
                System.out.print(row.get(Statistics.StatLong.PHASE1) + "\t");
                System.out.print(String.format("%.4f\t", row.get(Statistics.StatFloat.FREQ1)));
                System.out.print(row.get(Statistics.StatLong.PHASE2) + "\t");
                System.out.print(String.format("%.4f\t", row.get(Statistics.StatFloat.FREQ2)));
                System.out.print(row.get(Statistics.StatLong.PREF0) + "\t");
                System.out.print(row.get(Statistics.StatLong.PREF1) + "\t");
                System.out.print(row.get(Statistics.StatLong.PREF2) + "\t");
            }else System.out.print("NUUUUUUUUUUUUUUUUUUULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL nein nein nein");
        }

        System.out.println("\nTOTAL\t" + _stat.getTotalTrinucleotide() + "\t\t"
                + _stat.getTotalTrinucleotide() + "\t\t"
                + _stat.getTotalTrinucleotide() + "\n");
    }

    private static void printIData(IDataBase _data){
        System.out.println("NOM            : "+_data.getName());
        System.out.println("DATE           : "+_data.getModificationDate());
        System.out.println("STATE          : "+_data.getState());
        System.out.println("GENOME         : "+_data.getGenomeNumber());
        for(Statistics stat : _data.getStatistics().values()){
            System.out.println("TYPE           : "+stat.getType());
            printTriTable(stat);
        }
    }

    private static void serializableIData(IDataBase _data){
        printIData(_data);

        File fileOut = new File("Save/test_"+_data.getName()+".ser");
        ObjectOutputStream objectOut = null;
        ObjectInputStream objectIn = null;
        if (fileOut.exists()) {
            fileOut.delete();
        }
        File fileIn = null;
        try {
            fileOut.createNewFile();
            objectOut = new ObjectOutputStream(new FileOutputStream(fileOut));

            objectOut.writeObject((IDataBase)_data);
            objectOut.flush();
            objectOut.close();

            fileIn = new File("Save/test_"+_data.getName()+".ser");
            objectIn = new ObjectInputStream((new FileInputStream(fileIn)));
            IDataBase retour = (IDataBase) objectIn.readObject();
            if(retour != null){
                printIData(retour);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logs.exception(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Logs.exception(e);
        }
    }

    @Test
    void serializableTest() throws AddException, InvalidStateException {

        Path Path= Paths.get("Save");
        if(Files.notExists(Path))
        {
            try {
                Files.createDirectories(Path);
            } catch (IOException e) {
                Logs.exception(e);
            }

        }

        DataBase db = new DataBase("DataBase", _dataBase -> {
            serializableIData(_dataBase);
            /*printIData(_dataBase);

            File fileOut = new File("Save/test_"+_dataBase.getName()+".ser");
            ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            if (fileOut.exists()) {
                fileOut.delete();
            }
            File fileIn = null;
            try {
                fileOut.createNewFile();
                objectOut = new ObjectOutputStream(new FileOutputStream(fileOut));

                objectOut.writeObject(_dataBase);
                objectOut.flush();
                objectOut.close();

                fileIn = new File("Save/test_"+_dataBase.getName()+".ser");
                objectIn = new ObjectInputStream((new FileInputStream(fileIn)));
                DataBase retour = (DataBase) objectIn.readObject();
                if(retour != null){
                    printIData(retour);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logs.exception(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Logs.exception(e);
            }*/
            });
        db.start();

        Kingdom ki = new Kingdom("Kingdom", _kingdom -> {
            serializableIData(_kingdom);
            /*printIData(_kingdom);

            File fileOut = new File("Save/test_"+_kingdom.getName()+".ser");
            ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            if (fileOut.exists()) {
                fileOut.delete();
            }
            File fileIn = null;
            try {
                fileOut.createNewFile();
                objectOut = new ObjectOutputStream(new FileOutputStream(fileOut));

                objectOut.writeObject(_kingdom);
                objectOut.flush();
                objectOut.close();

                fileIn = new File("Save/test_"+_kingdom.getName()+".ser");
                objectIn = new ObjectInputStream((new FileInputStream(fileIn)));
                Kingdom retour = (Kingdom) objectIn.readObject();
                if(retour != null){
                    printIData(retour);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logs.exception(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Logs.exception(e);
            }*/
            });
        ki.start();
        db.addKingdom(ki);
        db.stop();

        Group gr = new Group("Group", _group -> {
            serializableIData(_group);
            /*printIData(_group);

            File fileOut = new File("Save/test_"+_group.getName()+".ser");
            ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            if (fileOut.exists()) {
                fileOut.delete();
            }
            File fileIn = null;
            try {
                fileOut.createNewFile();
                objectOut = new ObjectOutputStream(new FileOutputStream(fileOut));

                objectOut.writeObject(_group);
                objectOut.flush();
                objectOut.close();

                fileIn = new File("Save/test_"+_group.getName()+".ser");
                objectIn = new ObjectInputStream((new FileInputStream(fileIn)));
                Group retour = (Group) objectIn.readObject();
                if(retour != null){
                    printIData(retour);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logs.exception(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Logs.exception(e);
            }*/
            });
        gr.start();
        ki.addGroup(gr);
        ki.stop();

        SubGroup su = new SubGroup("SubGroup", _subGroup -> {
            serializableIData(_subGroup);
            /*printIData(_subGroup);

            File fileOut = new File("Save/test_"+_subGroup.getName()+".ser");
            ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            if (fileOut.exists()) {
                fileOut.delete();
            }
            File fileIn = null;
            try {
                fileOut.createNewFile();
                objectOut = new ObjectOutputStream(new FileOutputStream(fileOut));

                objectOut.writeObject(_subGroup);
                objectOut.flush();
                objectOut.close();

                fileIn = new File("Save/test_"+_subGroup.getName()+".ser");
                objectIn = new ObjectInputStream((new FileInputStream(fileIn)));
                SubGroup retour = (SubGroup) objectIn.readObject();
                if(retour != null){
                    printIData(retour);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logs.exception(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Logs.exception(e);
            }*/
            });
        su.start();
        gr.addSubGroup(su);
        gr.stop();

        Organism or = new Organism("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, _organism -> {
            serializableIData(_organism);
            /*printIData(_organism);

            File fileOut = new File("Save/test_"+_organism.getName()+".ser");
            ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            if (fileOut.exists()) {
                fileOut.delete();
            }
            File fileIn = null;
            try {
                fileOut.createNewFile();
                objectOut = new ObjectOutputStream(new FileOutputStream(fileOut));

                objectOut.writeObject(_organism);
                objectOut.flush();
                objectOut.close();

                fileIn = new File("Save/test_"+_organism.getName()+".ser");
                objectIn = new ObjectInputStream((new FileInputStream(fileIn)));
                Organism retour = (Organism) objectIn.readObject();
                if(retour != null){
                    printIData(retour);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logs.exception(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Logs.exception(e);
            }*/
            });
        or.start();
        su.addOrganism(or);
        su.stop();

        for (int r = 0; r < 3; ++r) {
        Replicon re = new Replicon(Statistics.Type.CHROMOSOME, "CR1");
            StringBuilder strBuf = new StringBuilder("AAAAAGATAAGCTAATTAAGCTATTGGGTTCATACCCCACTTATAAAGGT");
            strBuf.append("AATTATTAATTATGTAAAATTAATTAATATAAAATTTTTATTAGTTTAAT");
            strBuf.append("ATATTAATATATAATATATATATATATAAAATTTTATATTTATATATATA");
            strBuf.append("AGAACTATAATTATGTTTTCATTGAGATATATTTATATATTTAAATAAAT");
            strBuf.append("ATTTTATTATTTATATATATAAATATATTATTAAATTATTTATATTAATA");
            strBuf.append("ATTATAAAATGAATTGCCTGACGAAAAGGGTTACCTTGATAGGGTAAATC");
            strBuf.append("ATAAAGTTTATACTTTATTCATTAAATTATATTTAATAGAATTAAACTAT");
            strBuf.append("TTCCAAAAGCTTCAAAAACTTTTGTGCATCGTACACTAAAATATAGATAA");
            strBuf.append("AATTATTAATTATGTAAAATTAATTAATATAAAATTTTTATTAGTTTAAT");
            strBuf.append("ATATTAATATATAATATATATATATATAAAATTTTATATTTATATATATA");
            strBuf.append("AGAACTATAATTATGTTTTCATTGAGATATATTTATATATTTAAATAAAT");
            strBuf.append("ATTATAAAATGAATTGCCTGACGAAAAGGGTTACCTTGATAGGGTAAATC");
            strBuf.append("ATAAAGTTTATACTTTATTCATTAAATTATATTTAATAGAATTAAACTAT");
            strBuf.append("TTCCAAAAGCTTCAAAAACTTTTGTGCATCGTACACTAAAATATAGATAA");
            strBuf.append("TATATATATATTTATGTATTTATATAAAAATAACTCTTAT");
            re.addSequence(strBuf);
            assertEquals("CR1", re.getName());
            or.addReplicon(re);
        }
        or.stop();
        or.finish();
    }

}