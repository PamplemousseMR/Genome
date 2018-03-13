package Data.Tests;

import Data.*;
import Exception.AddException;
import Exception.InvalidStateException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GlobalTest {

    @org.junit.jupiter.api.Test
    void dataBaseTest() throws AddException, InvalidStateException {
        DataBase dataBase = new DataBase("d1", _dataBase -> {
        });
        assertEquals(IDataBase.State.CREATED, dataBase.getState());

        // Database start and stop
        Kingdom k1 = new Kingdom("k1", _kingdom -> {
        });
        assertEquals(false, dataBase.addKingdom(k1));
        dataBase.start();
        assertEquals(true, dataBase.addKingdom(k1));

        Kingdom k2 = new Kingdom("k2", _kingdom -> {
        });
        assertEquals(true, dataBase.addKingdom(k2));

        assertThrows(Exception.class, () -> dataBase.addKingdom(k2));
        dataBase.stop();

        Kingdom k3 = new Kingdom("k3", _kingdom -> {
        });
        assertEquals(false, dataBase.addKingdom(k3));
        assertEquals(IDataBase.State.STOPPED, dataBase.getState());

        // Group start and Kingdom start and stop
        for (Kingdom k : dataBase.getKingdoms()) {
            Group g1 = new Group("g1_" + k.getName(), _group -> {
            });
            g1.start();
            assertEquals(false, k.addGroup(g1));
            k.start();
            assertEquals(true, k.addGroup(g1));

            Group g2 = new Group("g2_" + k.getName(), _group -> {
            });
            g2.start();
            assertEquals(true, k.addGroup(g2));
            assertThrows(Exception.class, () -> k.addGroup(g2));
            k.stop();

            Group g3 = new Group("g3_" + k.getName(), _group -> {
            });
            g3.start();
            assertEquals(false, k.addGroup(g3));
            assertEquals(IDataBase.State.STOPPED, k.getState());
        }

        // Group stop
        for (Kingdom k : dataBase.getKingdoms()) {
            for (Group g : k.getGroups()) {
                SubGroup s1 = new SubGroup("s1_" + g.getName(), _subGroup -> {
                });
                assertEquals(true, g.addSubGroup(s1));

                SubGroup s2 = new SubGroup("s2_" + g.getName(), _subGroup -> {
                });
                assertEquals(true, g.addSubGroup(s2));
                assertThrows(Exception.class, () -> g.addSubGroup(s2));

                g.stop();
                SubGroup s3 = new SubGroup("s3_" + g.getName(), _subGroup -> {
                });
                assertEquals(false, g.addSubGroup(s3));
                assertEquals(IDataBase.State.STOPPED, g.getState());
            }
        }

        ArrayList<Organism> list = new ArrayList<>();

        // Subgroup start and stop
        for (Kingdom k : dataBase.getKingdoms()) {
            for (Group g : k.getGroups()) {
                for (SubGroup s : g.getSubGroups()) {
                    Organism o1 = new Organism("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, _organism -> {
                    });
                    assertEquals(false, s.addOrganism(o1));
                    s.start();
                    assertEquals(true, s.addOrganism(o1));
                    list.add(o1);

                    Organism o2 = new Organism("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, _organism -> {
                    });
                    assertEquals(true, s.addOrganism(o2));
                    assertThrows(Exception.class, () -> s.addOrganism(o2));
                    s.stop();
                    list.add(o2);

                    Organism o3 = new Organism("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, _organism -> {
                    });
                    assertEquals(false, s.addOrganism(o3));
                    assertEquals(IDataBase.State.STOPPED, s.getState());
                }
            }
        }

        for (Kingdom k : dataBase.getKingdoms()) {
            for (Group g : k.getGroups()) {
                for (SubGroup s : g.getSubGroups()) {
                    for (Organism o : s.getOrganisms()) {
                        o.start();
                        Replicon r1 = new Replicon(Replicon.Type.CHLOROPLAST, "r1_" + o.getName());
                        assertEquals(true, o.addReplicon(r1));
                        assertEquals(Replicon.Type.CHLOROPLAST, r1.getType());

                        Replicon r2 = new Replicon(Replicon.Type.CHLOROPLAST, "r2_" + o.getName());
                        assertEquals(true, o.addReplicon(r2));
                        assertEquals(Replicon.Type.CHLOROPLAST, r2.getType());

                        Replicon r3 = new Replicon(Replicon.Type.MITOCHONDRION, "r3_" + o.getName());
                        assertEquals(true, o.addReplicon(r3));
                        assertEquals(Replicon.Type.MITOCHONDRION, r3.getType());

                        assertThrows(Exception.class, () -> o.addReplicon(r3));
                        o.stop();
                    }
                }
            }
        }

        for (Kingdom k : dataBase.getKingdoms()) {
            for (Group g : k.getGroups()) {
                for (SubGroup s : g.getSubGroups()) {
                    for (Organism o : s.getOrganisms()) {
                        for (Replicon r : o.getReplicons()) {
                            StringBuilder sb = new StringBuilder("AAATTTCCCGGG");
                            assertTrue(r.addSequence(sb));
                            assertThrows(Exception.class, () -> r.addSequence(sb));
                        }
                    }
                }
            }
        }

        for (Organism o : list) {
            o.finish();
        }
    }

    @org.junit.jupiter.api.Test
    void nameTest() throws AddException, InvalidStateException {

        Kingdom k = new Kingdom("KINGDOM", _kingdom -> {
        });
        k.start();

        Group g = new Group("GROUP", _group -> {
        });
        g.start();
        k.addGroup(g);

        SubGroup s = new SubGroup("SUBGROUP", _subGroup -> {
        });
        s.start();
        g.addSubGroup(s);

        Organism o = new Organism("'Brassica napus' phytoplasma", 152753L, 1592820474201505800L, _organism -> {
        });
        s.addOrganism(o);

        assertEquals("KINGDOM", o.getKingdomName());
        assertEquals("GROUP", o.getGroupName());
        assertEquals("SUBGROUP", o.getSubGroupName());

        assertEquals("KINGDOM", s.getKingdomName());
        assertEquals("GROUP", s.getGroupName());

        assertEquals("KINGDOM", g.getKingdomName());

        s.stop();
        g.stop();
        k.stop();
    }

}