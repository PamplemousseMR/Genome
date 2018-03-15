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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SerializableTest {

    private static boolean equals(Statistics _stat, Statistics _statc) {
        return _stat == null && _statc == null || _stat != null && _statc != null && _stat.getType() == _statc.getType() && _stat.getTotalTrinucleotide() == _statc.getTotalTrinucleotide() && equals(_stat.getTable(), _statc.getTable()) && _stat.getValidCDSNumber() == _statc.getValidCDSNumber() && _stat.getInvalidCDSNumber() == _statc.getInvalidCDSNumber();
    }

    private static boolean equals(Tuple[] _tuple, Tuple[] _tuplec) {
        if (_tuple == null && _tuplec == null) return true;
        if (_tuple == null || _tuplec == null) return false;
        if (_tuple.length != _tuplec.length) return false;
        for (int i = 0; i < _tuple.length; ++i) {
            if (_tuple[i] == null || _tuplec[i] == null) return false;
            for (Statistics.StatLong l : Statistics.StatLong.values())
                if (_tuple[i].get(l) != _tuplec[i].get(l)) return false;
            for (Statistics.StatFloat f : Statistics.StatFloat.values())
                if (_tuple[i].get(f) != _tuplec[i].get(f)) return false;
        }
        return true;
    }

    private static boolean equals(IDataBase _data, IDataBase _datac) {
        if (_data == null && _datac == null) return true;
        if (_data == null || _datac == null) return false;
        for (Statistics.Type t : Statistics.Type.values()) {
            if (_data.getGenomeNumber().get(t) != null || _datac.getGenomeNumber().get(t) != null) {
                if (!equals(_data.getStatistics().get(t), _datac.getStatistics().get(t))) {
                    return false;
                }
                if (_data.getGenomeNumber().get(t).longValue() != (_datac.getGenomeNumber().get(t)).longValue()) {
                    return false;
                }
            }
        }
        return _data.getTotalOrganism() == _datac.getTotalOrganism()
                && _data.getValidCDSNumber() == _datac.getValidCDSNumber()
                && _data.getInvalidCDSNumber() == _datac.getInvalidCDSNumber();
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

        final int nb = 5, nbrep = 200;
        DataBase db = new DataBase("_DataBase", _dataBase -> {
            _dataBase.save();
            IDataBase retour = IDataBase.load(_dataBase.getName());
            assertTrue(equals(_dataBase, retour));
        });
        db.start();

        for (int k = 0; k < nb; k++) {
            Kingdom ki = new Kingdom(k + "__Kingdom", _kingdom -> {
                _kingdom.save();
                IDataBase retour = IDataBase.load(_kingdom.getName());
                assertTrue(equals(_kingdom, retour));
            });
            ki.start();
            db.addKingdom(ki);

            for (int g = 0; g < nb; g++) {
                Group gr = new Group(k + "_" + g + "__Group", _group -> {
                    _group.save();
                    IDataBase retour = IDataBase.load(_group.getName());
                    assertTrue(equals(_group, retour));
                });
                gr.start();
                ki.addGroup(gr);

                for (int s = 0; s < nb; s++) {
                    SubGroup su = new SubGroup(k + "_" + g + "_" + s + "__SubGroup", _subGroup -> {
                        _subGroup.save();
                        IDataBase retour = IDataBase.load(_subGroup.getName());
                        assertTrue(equals(_subGroup, retour));
                    });
                    su.start();
                    gr.addSubGroup(su);

                    for (int o = 0; o < nb; o++) {
                        Organism or = new Organism(k + "_" + g + "_" + s + "_" + o + "__Organism", 152753L, 1592820474201505800L, _organism -> {
                            _organism.save();
                            IDataBase retour = IDataBase.load(_organism.getName());
                            assertTrue(equals(_organism, retour));
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