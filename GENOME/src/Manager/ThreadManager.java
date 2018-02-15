package Manager;

import Utils.Logs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ThreadManager {

    /**
     * Executor which get a task and execute it
     */
    final class Executor implements Runnable{

        @Override
        public void run() {

            while(true) {

                ITask todo = null;

                // Get task to do
                m_lockArray.lock();
                {
                    // Wait until they are data
                    while (m_downloads.isEmpty() && m_computes.isEmpty()) {
                        if(!isRunning()){
                            m_lockArray.unlock();
                            return;
                        }
                        try {
                            m_condArray.await();
                        } catch (InterruptedException e) {
                            Logs.exception(e);
                        }
                    }

                    // Choose a data
                    if (!m_downloads.isEmpty() && m_actualIDownloadRun < m_iDownloadMax) {
                        ++m_actualIDownloadRun;
                        todo = m_downloads.removeFirst();
                    } else if (!m_computes.isEmpty() && m_actualIComputeRun < m_iComputeMax) {
                        ++m_actualIComputeRun;
                        todo = m_computes.removeFirst();
                    } else if (!m_downloads.isEmpty()) {
                        ++m_actualIDownloadRun;
                        todo = m_downloads.removeFirst();
                    } else if (!m_computes.isEmpty()){
                        ++m_actualIComputeRun;
                        todo = m_computes.removeFirst();
                    } else {
                        Logs.warning("Concurrency error");
                    }

                    // Log
                    if(todo != null){
                        if(m_computes.size() + m_downloads.size() < m_nbThreadMax){
                            m_condPush.signalAll();
                        }
                        Logs.info("Task '" + todo.getName() + "' of type " + todo.getTaskType() + " is chosen : remains " + m_downloads.size() + " downloading task and " + m_computes.size() + " computing task");
                    }

                }
                m_lockArray.unlock();

                if (todo != null) {

                    // Run task
                    try {
                        todo.run();
                    } catch(Exception e){
                        Logs.exception(e);
                    }

                    // Free
                    m_lockArray.lock();
                    {
                        if (todo.getTaskType() == ITask.TaskType.DOWNLOAD) {
                            --m_actualIDownloadRun;
                        } else {
                            --m_actualIComputeRun;
                        }
                        Logs.info("Task '" + todo.getName() + "' of type " + todo.getTaskType() + "' is finished");
                    }
                    m_lockArray.unlock();

                }else{
                    Logs.warning("No task to do");
                }
            }

        }
    }

    // Configuration

    private static int s_nbThreadMax;
    private static int s_iDownloadMax;
    private static int s_iComputeMax;

    /**
     * Configure ThreadManager's parameters
     * @param _nbThreadMax, the max number of threads
     * @param _iDownloadMax, the max number of download thread
     * @throws Exception, if the parameters are not corrects
     */
    public static void configure(int _nbThreadMax, int _iDownloadMax) throws Exception{
        if(_iDownloadMax <= 0 || _nbThreadMax <= 0){
            throw new Exception("Value can't be negative");
        }
        if(_iDownloadMax > _nbThreadMax){
            throw new Exception("_iDownloadMax must be inferior at _nbThreadMax");
        }

        s_nbThreadMax = _nbThreadMax;
        s_iDownloadMax = _iDownloadMax;
        s_iComputeMax = _nbThreadMax - _iDownloadMax;
    }

    // Singleton

    private static ThreadManager s_instance;

    private final int m_nbThreadMax;
    private final int m_iDownloadMax;
    private final int m_iComputeMax;

    private volatile ArrayList<Thread> m_threads;
    private volatile boolean m_running;
    private final Lock m_runningLock;

    private volatile LinkedList<IDownload> m_downloads;
    private volatile int m_actualIDownloadRun;

    private volatile LinkedList<ICompute> m_computes;
    private volatile int m_actualIComputeRun;

    private final Lock m_lockArray;
    private final Condition m_condArray;
    private final Condition m_condPush;

    /**
     * Instantiate all threads
     */
    private ThreadManager() {

        m_nbThreadMax = s_nbThreadMax;
        m_iDownloadMax = s_iDownloadMax;
        m_iComputeMax = s_iComputeMax;

        Logs.info("Number of threads : " + s_nbThreadMax);
        Logs.info("Number of download threads : " + m_iDownloadMax);
        Logs.info("Number of compute threads : " + m_iComputeMax);

        m_threads = new ArrayList<>(s_nbThreadMax);
        m_running = true;
        m_runningLock = new ReentrantLock();

        m_downloads = new LinkedList<>();
        m_actualIDownloadRun = 0;

        m_computes = new LinkedList<>();
        m_actualIComputeRun = 0;

        m_lockArray = new ReentrantLock();
        m_condArray = m_lockArray.newCondition();
        m_condPush = m_lockArray.newCondition();

        for(int i=0 ; i<s_nbThreadMax ; ++i){
            Thread thr = new Thread(new Executor());
            thr.setPriority(Thread.MAX_PRIORITY);
            m_threads.add(thr);
            thr.start();
        }
    }

    /**
     * Check if the threads are running
     * @return the running state
     */
    private boolean isRunning(){
        boolean res;
        m_runningLock.lock();
        {
            res = m_running;
        }
        m_runningLock.unlock();
        return res;
    }

    /**
     * Get the only instance
     */
    public static ThreadManager getInstance() {
        if(s_instance == null){
            s_instance = new ThreadManager();
        }
        return s_instance;
    }

    /**
     * Blocking method which wait until all task are finished
     */
    public void finalizeThreadManager(){
        m_runningLock.lock();
        {
            m_running = false;
        }
        m_runningLock.unlock();

        m_lockArray.lock();
        {
            m_condArray.signalAll();
        }
        m_lockArray.unlock();

        for(Thread thr : m_threads){
            try {
                thr.join();
            } catch (InterruptedException e) {
                Logs.exception(e);
            }
        }
        Logs.info("Finalized");
    }

    /**
     * Push a new downloading task
     * @param _iDownload, the task to push
     * @return the insertion success
     */
    public boolean pushDownloadTask(IDownload _iDownload){
        boolean res = false;
        m_lockArray.lock();
        try {
            while(m_computes.size() + m_downloads.size() >= m_nbThreadMax){
                m_condPush.await();
            }
            res = m_downloads.add(_iDownload);
            if (res) {
                m_condArray.signal();
                Logs.info("Add downloading task : " + _iDownload.getName());
            }
        } catch(Exception e){
            Logs.exception(e);
        } finally {
            m_lockArray.unlock();
        }
        return res;
    }

    /**
     * Push a new computing task
     * @param _iCompute, the task to push
     * @return the insertion success
     */
    public boolean pushComputeTask(ICompute _iCompute){
        boolean res = false;
        m_lockArray.lock();
        try{
            while(m_computes.size() + m_downloads.size() >= m_nbThreadMax){
                m_condPush.await();
            }
            res = m_computes.add(_iCompute);
            if(res) {
                m_condArray.signal();
                Logs.info("Add computing task : " + _iCompute.getName());
            }
        } catch(Exception e){
            Logs.exception(e);
        } finally {
            m_lockArray.unlock();
        }
        return res;
    }

}
