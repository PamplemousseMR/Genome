package Data.Tests;

import Data.IDataBase;
import Exception.InvalidStateException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateTest {

    private final class IStateTest extends IDataBase{
        IStateTest(){
            super("TEST");
        }

        @Override
        protected void finish() throws InvalidStateException {
            super.finish();
        }
    }

    @org.junit.jupiter.api.Test
    void stateTest() throws InvalidStateException {

        IStateTest db = new IStateTest();

        assertEquals(IDataBase.State.CREATED, db.getState());

        assertThrows(InvalidStateException.class, db::stop);
        assertThrows(InvalidStateException.class, db::finish);
        db.start();

        assertEquals(IDataBase.State.STARTED, db.getState());

        assertThrows(InvalidStateException.class, db::start);
        assertThrows(InvalidStateException.class, db::finish);
        db.stop();

        assertEquals(IDataBase.State.STOPPED, db.getState());

        assertThrows(InvalidStateException.class, db::start);
        assertThrows(InvalidStateException.class, db::stop);
        db.finish();

        assertEquals(IDataBase.State.FINISHED, db.getState());

        assertThrows(InvalidStateException.class, db::start);
        assertThrows(InvalidStateException.class, db::stop);
        assertThrows(InvalidStateException.class, db::finish);
    }

}