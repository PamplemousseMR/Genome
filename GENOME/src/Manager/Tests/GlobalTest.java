package Manager.Tests;

import Manager.ICompute;
import Manager.IDownload;
import Manager.ThreadManager;
import Utils.Logs;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalTest {

    @org.junit.jupiter.api.Test
    void threadManagerTest() throws Exception {

        ThreadManager.configure(Runtime.getRuntime().availableProcessors(),Runtime.getRuntime().availableProcessors()/2);
        ThreadManager thr = ThreadManager.getInstance();

        // Store results of each threads
        LinkedList<LinkedList<Boolean>> results = new LinkedList<>();

        for(int i=0 ; i<30 ; ++i) {
            LinkedList<Boolean> res1 = new LinkedList<>();
            results.add(res1);
            LinkedList<Boolean> res2 = new LinkedList<>();
            results.add(res2);

            assertTrue(thr.pushDownloadTask(new IDownload("") {
                @Override
                public void run() {
                    for(long j=0 ; j<10 ; ++j) {
                        assertTrue(res1.add(true));
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Logs.exception(e);
                        }
                    }
                }
            }));

            assertTrue(thr.pushComputeTask(new ICompute("") {
                @Override
                public void run() {
                    for (long j = 0; j < 10; ++j) {
                        assertTrue(res2.add(true));
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Logs.exception(e);
                        }
                    }
                }
            }));
        }

        // Blocked until all tasks are finished
        thr.finalizeThreadManager();

        for(LinkedList<Boolean> li : results) {
            assertEquals(10, li.size());
        }
    }

}