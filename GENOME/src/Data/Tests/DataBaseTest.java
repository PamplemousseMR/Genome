package Data.Tests;

import Data.DataBase;
import Data.Kingdom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataBaseTest {

    static private DataBase db;

    @BeforeAll
    static void setUpTest() {
        db = DataBase.getInstance();
        try {
            db.start();
        }catch (Exception e){
            assertNull(e);
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            db.stop();
        }catch (Exception e){
            assertNull(e);
        }
    }

    @org.junit.jupiter.api.Test
    void getInstanceTest() {
        assertEquals(db, DataBase.getInstance());
    }

    @org.junit.jupiter.api.Test
    void addKingdomTest() {
        db.addKingdom(new Kingdom(""));
        assertEquals(1, db.getKingdoms().size());
        db.addKingdom(new Kingdom(""));
        assertEquals(2, db.getKingdoms().size());
    }

    @org.junit.jupiter.api.Test
    void getKingdomsTest() {
        Kingdom k1 = new Kingdom("one");
        Kingdom k2 = new Kingdom("two");
        db.addKingdom(k1);
        db.addKingdom(k2);
        assertEquals(true, db.getKingdoms().contains(k1));
        assertEquals(true, db.getKingdoms().contains(k2));

        Kingdom k3 = new Kingdom("three");
        assertEquals(false, db.getKingdoms().contains(k3));
    }

    @org.junit.jupiter.api.Test
    void getModificationDateTest() {
        Calendar caldb = Calendar.getInstance();
        caldb.setTime(db.getModificationDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        assertEquals(caldb.get(Calendar.YEAR), cal.get(Calendar.YEAR));
        assertEquals(caldb.get(Calendar.MONTH), cal.get(Calendar.MONTH));
        assertEquals(caldb.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    @org.junit.jupiter.api.Test
    void getNameTest() {
        assertEquals(DataBase.s_NAME, db.getName());
    }

    @org.junit.jupiter.api.Test
    void getStatisticsTest() {
    }
}