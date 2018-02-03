package Data.Tests;

import Data.IState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IStateTest {

    private final class StateTest extends IState{
        StateTest(){
            super("TEST");
        }

        @Override
        public void finish() throws Exception{
            super.finish();
        }
    }

    @org.junit.jupiter.api.Test
    void iStateTest() throws Exception {

        StateTest db = new StateTest();

        assertEquals(IState.State.CREATED, db.getState());

        assertThrows(Exception.class, db::stop);
        assertThrows(Exception.class, db::finish);
        db.start();

        assertEquals(IState.State.STARTED, db.getState());

        assertThrows(Exception.class, db::start);
        assertThrows(Exception.class, db::finish);
        db.stop();

        assertEquals(IState.State.STOPPED, db.getState());

        assertThrows(Exception.class, db::start);
        assertThrows(Exception.class, db::stop);
        db.finish();

        assertEquals(IState.State.FINISHED, db.getState());

        assertThrows(Exception.class, db::start);
        assertThrows(Exception.class, db::stop);
        assertThrows(Exception.class, db::finish);
    }

}