package Data.Tests;

import Data.IDataBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateTest {

    private final class IStateTest extends IDataBase{
        IStateTest(){
            super("TEST");
        }

        @Override
        public void finish() throws Exception{
            super.finish();
        }
    }

    @org.junit.jupiter.api.Test
    void stateTest() throws Exception {

        IStateTest db = new IStateTest();

        assertEquals(IDataBase.State.CREATED, db.getState());

        assertThrows(Exception.class, db::stop);
        assertThrows(Exception.class, db::finish);
        db.start();

        assertEquals(IDataBase.State.STARTED, db.getState());

        assertThrows(Exception.class, db::start);
        assertThrows(Exception.class, db::finish);
        db.stop();

        assertEquals(IDataBase.State.STOPPED, db.getState());

        assertThrows(Exception.class, db::start);
        assertThrows(Exception.class, db::stop);
        db.finish();

        assertEquals(IDataBase.State.FINISHED, db.getState());

        assertThrows(Exception.class, db::start);
        assertThrows(Exception.class, db::stop);
        assertThrows(Exception.class, db::finish);
    }

}