package Manager.Tests;

import Manager.ICompute;
import Manager.IDownload;
import Manager.ThreadManager;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ThreadManagerTest {

    private static ThreadManager threadManager;

    @BeforeAll
    static void setUp() throws Exception {
        ThreadManager.configure(Runtime.getRuntime().availableProcessors(),Runtime.getRuntime().availableProcessors()/2);
        threadManager = ThreadManager.getInstance();
    }

    @Test
    void configure() {
        assertThrows(Exception.class, () -> ThreadManager.configure(0,0));
        assertThrows(Exception.class, () -> ThreadManager.configure(0,1));
        assertThrows(Exception.class, () -> ThreadManager.configure(1,0));
        assertThrows(Exception.class, () -> ThreadManager.configure(-1,1));
        assertThrows(Exception.class, () -> ThreadManager.configure(1,-1));
        assertThrows(Exception.class, () -> ThreadManager.configure(2,4));
    }

    @Test
    void getInstance() {
        assertSame(threadManager, ThreadManager.getInstance());
    }

    @AfterAll
    static void finalizeThreadManager() {
        assertTimeout(Duration.ofSeconds(1),() -> threadManager.finalizeThreadManager());
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