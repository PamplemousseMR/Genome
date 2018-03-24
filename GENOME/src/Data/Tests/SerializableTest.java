package Data.Tests;

import Data.*;
import Exception.AddException;
import Exception.InvalidStateException;
import Utils.Logs;
import Utils.Options;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerializableTest {

    private static void myAssertEquals(Statistics _stat, Statistics _statc) {
        if (_stat == null && _statc == null) return;
        assertNotNull(_stat);
        assertNotNull(_statc);
        assertEquals(_stat.getType(), _statc.getType());
        assertEquals( _stat.getTotalTrinucleotide() , _statc.getTotalTrinucleotide());
        myAssertEquals(_stat.getTable(), _statc.getTable());
        assertEquals( _stat.getCDSNumber(), _statc.getCDSNumber());
        assertEquals( _stat.getValidCDSNumber(), _statc.getValidCDSNumber());
    }

    private static void myAssertEquals(Tuple[] _tuple, Tuple[] _tuplec) {
        if (_tuple == null && _tuplec == null) return;
        assertNotNull(_tuple);
        assertNotNull(_tuple);
        assertEquals(_tuple.length, _tuplec.length);
        for (int i = 0; i < _tuple.length; ++i) {
            assertNotNull(_tuple[i]);
            assertNotNull(_tuplec[i]);
            for (Statistics.StatLong l : Statistics.StatLong.values())
                assertEquals(_tuple[i].get(l), _tuplec[i].get(l));
            for (Statistics.StatFloat f : Statistics.StatFloat.values())
                assertEquals(_tuple[i].get(f), _tuplec[i].get(f));
        }
    }

    private void myAssertEquals(IDataBase _data, IDataBase _datac) {
        if (_data == null && _datac == null) return;
        assertNotNull(_data);
        assertNotNull(_datac);
        for (Statistics.Type t : Statistics.Type.values()) {
            if (_data.getGenomeNumber().get(t) != null || _datac.getGenomeNumber().get(t) != null) {
                myAssertEquals(_data.getStatistics().get(t), _datac.getStatistics().get(t));
                assertEquals(_data.getGenomeNumber().get(t).longValue(),_datac.getGenomeNumber().get(t).longValue());
            }
        }
        assertEquals( _data.getTotalOrganism() , _datac.getTotalOrganism());
        assertEquals( _data.getCDSNumber() , _datac.getCDSNumber());
        assertEquals( _data.getValidCDSNumber() , _datac.getValidCDSNumber());
    }

    @Test
    void serializableTest() throws AddException, InvalidStateException {

        Path Path = Paths.get(Options.getSerializeDirectory());
        if (Files.notExists(Path)) {
            try {
                Files.createDirectories(Path);
            } catch (IOException e) {
                Logs.exception(e);
            }

        }

        final int nb = 2, nbRep = 200;
        DataBase db = new DataBase("GENBANK", _dataBase -> {
            _dataBase.save();
            DataBase loaded = DataBase.load(_dataBase.getName(), _arg -> {});
            myAssertEquals(_dataBase, loaded);
        });
        db.start();

        for (int k = 0; k < nb; k++) {
            Kingdom ki = new Kingdom("KNG" + k, _kingdom -> {
                _kingdom.save();
                Kingdom loaded = Kingdom.load(_kingdom.getName(), _kingdom.getParent(), _arg -> {});
                myAssertEquals(_kingdom, loaded);
            });
            db.addKingdom(ki);
            ki.start();

            for (int g = 0; g < nb; g++) {
                Group gr = new Group("GRP" + g, _group -> {
                    _group.save();
                    Group loaded = Group.load(_group.getName(), _group.getParent(), _arg -> {});
                    myAssertEquals(_group, loaded);
                });
                ki.addGroup(gr);
                gr.start();

                for (int s = 0; s < nb; s++) {
                    SubGroup su = new SubGroup("SUB" + s, _subGroup -> {
                        _subGroup.save();
                        SubGroup loaded = SubGroup.load(_subGroup.getName(), _subGroup.getParent(), _arg -> {});
                        myAssertEquals(_subGroup, loaded);

                        if (nb == 2) { //TEST EN DUR DE L'UNLOAD
                            try {
                                _subGroup.getParent().addSubGroup(loaded);
                                loaded.start();
                            }catch (Exception e){e.printStackTrace();}
                            //first time to unload it
                            //second time to get the last
                            Organism loaded_child = Organism.load(_subGroup.getOrganisms().get(0).getName(), 45L, 54L,  loaded, true, _arg -> {});
                            loaded_child = Organism.load(_subGroup.getOrganisms().get(0).getName(), 45L, 54L,  loaded, false, _arg -> {});
                            myAssertEquals(loaded, loaded_child);
                            try {
                                loaded.stop();
                            }catch (Exception e){e.printStackTrace();}
                        }
                    });
                    gr.addSubGroup(su);
                    su.start();

                    for (int o = 0; o < nb; o++) {
                        Organism or = new Organism("ORG" + o, 152753L, 1592820474201505800L, _organism -> {
                            _organism.save();
                            Organism loaded = Organism.load(_organism.getName(), 45L, 54L, _organism.getParent(), false, _arg -> {});
                            myAssertEquals(_organism, loaded);
                            Date loadedDate = Organism.loadDate(_organism.getName(), _organism.getParent());
                            assertEquals(_organism.getModificationDate(),loadedDate);
                        });
                        su.addOrganism(or);
                        or.start();

                        for (int r = 0; r < nbRep; ++r) {
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
                            ArrayList<StringBuilder> sequences = new ArrayList<>();
                            sequences.add(strBuf);
                            Replicon re = new Replicon(Statistics.Type.CHROMOSOME, "CR1", 2, 1, sequences);
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