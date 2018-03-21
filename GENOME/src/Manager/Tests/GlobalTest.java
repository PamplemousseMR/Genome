package Manager.Tests;

import Manager.ICompute;
import Manager.IDownload;
import Manager.ThreadManager;
import Utils.Logs;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalTest {

    @org.junit.jupiter.api.Test
    void threadManagerTest() throws Exception {

        ThreadManager thr = new ThreadManager(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() / 2);

        // Store results of each threads
        ArrayList<ArrayList<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < 30; ++i) {
            ArrayList<Boolean> res1 = new ArrayList<>();
            results.add(res1);
            ArrayList<Boolean> res2 = new ArrayList<>();
            results.add(res2);

            assertTrue(thr.pushDownloadTask(new IDownload("" + i) {
                @Override
                public void run() {
                    for (long j = 0; j < 10; ++j) {
                        assertTrue(res1.add(true));
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Logs.exception(e);
                        }
                    }
                }
            }));

            assertTrue(thr.pushComputeTask(new ICompute("" + i) {
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

        for (ArrayList<Boolean> li : results) {
            assertEquals(10, li.size());
        }
    }

}