package Manager.Tests;

import Manager.ICompute;
import Manager.IDownload;
import Manager.ThreadManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadManagerTest {

    private static ThreadManager threadManager;

    @BeforeAll
    static void setUp() throws Exception {
        threadManager = new ThreadManager(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() / 2);

    }

    @AfterAll
    static void finalizeThreadManager() {
        assertTimeout(Duration.ofSeconds(1), () -> threadManager.finalizeThreadManager());
    }

    @Test
    void pushDownloadTask() {
        assertTrue(threadManager.pushDownloadTask(new IDownload("") {
            @Override
            public void run() {
            }
        }));
    }

    @Test
    void pushComputeTask() {
        assertTrue(threadManager.pushComputeTask(new ICompute("") {
            @Override
            public void run() {
            }
        }));
    }
}