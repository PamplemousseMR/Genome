package Data.Tests;

import Data.DataBase;
import Data.Kingdom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.Calendar;
import java.util.Date;
import Exception.InvalidStateException;
import Exception.AddException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class DataBaseTest {

    static private DataBase db;

    @BeforeAll
    static void setUpTest() throws InvalidStateException {
        db = DataBase.getInstance();
        db.start();
    }

    @AfterAll
    static void tearDown() throws InvalidStateException {
        db.stop();
    }

    @org.junit.jupiter.api.Test
    void getInstanceTest() {
        assertSame(db, DataBase.getInstance());
    }

    @org.junit.jupiter.api.Test
    void addKingdomTest() throws AddException {
        db.addKingdom(new Kingdom(""));
        assertEquals(1, db.getKingdoms().size());
        db.addKingdom(new Kingdom(""));
        assertEquals(2, db.getKingdoms().size());
    }

    @org.junit.jupiter.api.Test
    void getKingdomsTest() throws AddException {
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

}