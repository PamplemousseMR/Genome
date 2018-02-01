package Data.Tests;

import Data.IState;

import static org.junit.jupiter.api.Assertions.*;

class IStateTest {

    private class StateTest extends IState{
        public StateTest(){
            super("TEST");
        }

        public boolean finish() throws Exception{
            super.finish();
            return true;
        }
    }

    @org.junit.jupiter.api.Test
    public void iStateTest(){

        StateTest db = new StateTest();

        assertEquals(IState.State.CREATED, db.getState());

        assertThrows(Exception.class, ()->{db.stop();});
        assertThrows(Exception.class, ()->{db.finish();});
        try {
            db.start();
        }catch (Exception e){
            assertNull(e);
        }

        assertEquals(IState.State.STARTED, db.getState());

        assertThrows(Exception.class, ()->{db.start();});
        assertThrows(Exception.class, ()->{db.finish();});
        try {
            db.stop();
        }catch (Exception e){
            assertNull(e);
        }

        assertEquals(IState.State.STOPPED, db.getState());

        assertThrows(Exception.class, ()->{db.start();});
        assertThrows(Exception.class, ()->{db.stop();});
        try {
            db.finish();
        }catch (Exception e){
            assertNull(e);
        }

        assertEquals(IState.State.FINISHED, db.getState());

        assertThrows(Exception.class, ()->{db.start();});
        assertThrows(Exception.class, ()->{db.stop();});
        assertThrows(Exception.class, ()->{db.finish();});
    }

}