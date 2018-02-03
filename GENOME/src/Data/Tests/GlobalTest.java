package Data.Tests;

import Data.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class GlobalTest {

    @org.junit.jupiter.api.Test
    void dataBaseTest() throws Exception{
        DataBase dataBase = DataBase.getInstance();
        assertEquals(IState.State.CREATED,dataBase.getState());

        // Database start and stop
        Kingdom k1 = new Kingdom("k1");
        assertEquals(false,dataBase.addKingdom(k1));
        dataBase.start();
        assertEquals(true,dataBase.addKingdom(k1));

        Kingdom k2 = new Kingdom("k2");
        assertEquals(true,dataBase.addKingdom(k2));

        assertThrows(Exception.class, () -> dataBase.addKingdom(k2));
        dataBase.stop();

        Kingdom k3 = new Kingdom("k3");
        assertEquals(false,dataBase.addKingdom(k3));
        assertEquals(IState.State.STOPPED,dataBase.getState());

        // Group start and Kingdom start and stop
        for(Kingdom k : dataBase.getKingdoms()){
            Group g1 = new Group("g1_"+k.getName());
            g1.start();
            assertEquals(false,k.addGroup(g1));
            k.start();
            assertEquals(true,k.addGroup(g1));

            Group g2 = new Group("g2_"+k.getName());
            g2.start();
            assertEquals(true,k.addGroup(g2));
            assertThrows(Exception.class, () -> k.addGroup(g2));
            k.stop();

            Group g3 = new Group("g3_"+k.getName());
            g3.start();
            assertEquals(false,k.addGroup(g3));
            assertEquals(IState.State.STOPPED,k.getState());
        }

        // Group stop
        for(Kingdom k : dataBase.getKingdoms()) {
            for(Group g : k.getGroups()) {
                SubGroup s1 = new SubGroup("s1_"+g.getName());
                assertEquals(true,g.addSubGroup(s1));

                SubGroup s2 = new SubGroup("s2_"+g.getName());
                assertEquals(true,g.addSubGroup(s2));
                assertThrows(Exception.class, () -> g.addSubGroup(s2));

                g.stop();
                SubGroup s3 = new SubGroup("s3_"+g.getName());
                assertEquals(false,g.addSubGroup(s3));
                assertEquals(IState.State.STOPPED,g.getState());
            }
        }

        LinkedList<Organism> list = new LinkedList<>();

        // Subgroup start and stop
        for(Kingdom k : dataBase.getKingdoms()) {
            for(Group g : k.getGroups()) {
                for(SubGroup s : g.getSubGroups()) {
                    Organism o1 = new Organism("o1_"+s.getName());
                    assertEquals(false,s.addOrganism(o1));
                    s.start();
                    assertEquals(true,s.addOrganism(o1));
                    list.add(o1);

                    Organism o2 = new Organism("o2_"+s.getName());
                    assertEquals(true,s.addOrganism(o2));
                    assertThrows(Exception.class, () -> s.addOrganism(o2));
                    s.stop();
                    list.add(o2);

                    Organism o3 = new Organism("o3_"+s.getName());
                    assertEquals(false,s.addOrganism(o3));
                    assertEquals(IState.State.STOPPED,s.getState());
                }
            }
        }

        for(Kingdom k : dataBase.getKingdoms()) {
            for(Group g : k.getGroups()) {
                for(SubGroup s : g.getSubGroups()) {
                    for(Organism o : s.getOrganisms()) {
                        Replicon r1 = new Replicon(Replicon.Type.CHLOROPLAST,"r1_"+o.getName());
                        assertEquals(true, o.addReplicon(r1));
                        assertEquals(Replicon.Type.CHLOROPLAST, r1.getType());

                        Replicon r2 = new Replicon(Replicon.Type.CHLOROPLAST,"r2_"+o.getName());
                        assertEquals(true, o.addReplicon(r2));
                        assertEquals(Replicon.Type.CHLOROPLAST, r2.getType());

                        Replicon r3 = new Replicon(Replicon.Type.MITOCHONDRION,"r3_"+o.getName());
                        assertEquals(true, o.addReplicon(r3));
                        assertEquals(Replicon.Type.MITOCHONDRION, r3.getType());

                        assertThrows(Exception.class, () -> o.addReplicon(r3));
                    }
                }
            }
        }

        for(Kingdom k : dataBase.getKingdoms()) {
            for(Group g : k.getGroups()) {
                for(SubGroup s : g.getSubGroups()) {
                    for(Organism o : s.getOrganisms()) {
                        for(Replicon r : o.getReplicons()){
                            StringBuffer sb = new StringBuffer("AAATTTCCCGGG");
                            assertTrue(r.addSequence(sb));
                            assertThrows(Exception.class, () -> r.addSequence(sb));
                        }
                    }
                }
            }
        }

        for(Organism o : list) {
            assertTrue(o.finish());
        }
    }

    @org.junit.jupiter.api.Test
    void nameTest() throws Exception{

        Kingdom k = new Kingdom("KINGDOM");
        k.start();

        Group g = new Group("GROUP");
        g.start();
        k.addGroup(g);

        SubGroup s = new SubGroup("SUBGROUP");
        s.start();
        g.addSubGroup(s);

        Organism o = new Organism("ORGANISM");
        s.addOrganism(o);

        assertEquals("KINGDOM",o.getKingdomName());
        assertEquals("GROUP",o.getGroupName());
        assertEquals("SUBGROUP",o.getSubGroupName());

        assertEquals("KINGDOM",s.getKingdomName());
        assertEquals("GROUP",s.getGroupName());

        assertEquals("KINGDOM",g.getKingdomName());

        s.stop();
        g.stop();
        k.stop();
    }

}