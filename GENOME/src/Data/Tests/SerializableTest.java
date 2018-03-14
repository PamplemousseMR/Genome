package Data.Tests;

import Data.*;
import Exception.AddException;
import Exception.InvalidStateException;
import Utils.Logs;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        final int nb = 5, nbrep = 200;
        DataBase db = new DataBase("_DataBase", _dataBase -> {
            _dataBase.save();
            IDataBase retour = IDataBase.s_load(_dataBase.getName());
            assertTrue(_dataBase.equals(retour));
            });
        db.start();

        for (int k = 0 ; k < nb ; k++) {
            Kingdom ki = new Kingdom(k+"__Kingdom", _kingdom -> {
                _kingdom.save();
                IDataBase retour = IDataBase.s_load(_kingdom.getName());
                assertTrue(_kingdom.equals(retour));
                });
            ki.start();
            db.addKingdom(ki);

            for (int g = 0; g < nb ; g++) {
                Group gr = new Group(k+"_"+g+"__Group", _group -> {
                    _group.save();
                    IDataBase retour = IDataBase.s_load(_group.getName());
                    assertTrue(_group.equals(retour));
                });
                gr.start();
                ki.addGroup(gr);

                for(int s = 0; s < nb ; s++) {
                    SubGroup su = new SubGroup(k+"_"+g+"_"+s+"__SubGroup", _subGroup -> {
                        _subGroup.save();
                        IDataBase retour = IDataBase.s_load(_subGroup.getName());
                        assertTrue(_subGroup.equals(retour));
                    });
                    su.start();
                    gr.addSubGroup(su);

                    for(int o = 0; o< nb ; o++) {
                        Organism or = new Organism(k+"_"+g+"_"+s+"_"+o+"__Organism", 152753L, 1592820474201505800L, _organism -> {
                            _organism.save();
                            IDataBase retour = IDataBase.s_load(_organism.getName());
                            assertTrue(_organism.equals(retour));
                        });
                        or.start();
                        su.addOrganism(or);

                        for (int r = 0; r < nbrep; ++r) {
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
                    su.stop();
                }
                gr.stop();
            }
            ki.stop();
        }
        db.stop();
    }

}