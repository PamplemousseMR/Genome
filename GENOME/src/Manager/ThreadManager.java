package Manager;

import Utils.Logs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ThreadManager {


    private final int m_nbThreadMax;
    private final Lock m_runningLock;
    private final Lock m_lockArray;
    private final Condition m_condArray;
    private final Condition m_condPush;
    private final ArrayList<Thread> m_threads;
    private final LinkedList<ITask> m_task;
    private volatile boolean m_running;

    /**
     * Instantiate all threads
     */
    public ThreadManager(int _nbThreadMax) {

        m_nbThreadMax = _nbThreadMax;

        Logs.info("Number of threads : " + m_nbThreadMax, true);

        m_threads = new ArrayList<>(m_nbThreadMax);
        m_running = true;
        m_runningLock = new ReentrantLock();

        m_task = new LinkedList<>();

        m_lockArray = new ReentrantLock();
        m_condArray = m_lockArray.newCondition();
        m_condPush = m_lockArray.newCondition();

        for (int i = 0; i < m_nbThreadMax; ++i) {
            final Thread thr = new Thread(new Executor(i));
            thr.setPriority(Thread.MAX_PRIORITY);
            m_threads.add(thr);
            thr.start();
        }
    }

    /**
     * Check if the threads are running
     *
     * @return the running state
     */
    private boolean isRunning() {
        final boolean res;
        m_runningLock.lock();
        {
            res = m_running;
        }
        m_runningLock.unlock();
        return res;
    }

    /**
     * Blocking method which wait until all task are finished
     */
    public void finalizeThreadManager() {
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

        for (Thread thr : m_threads) {
            try {
                thr.join();
            } catch (InterruptedException e) {
                Logs.warning("Unable to join thread : " + thr.getName());
                Logs.exception(e);
            }
        }
        Logs.info("Finalized", true);
    }

    /**
     * Push a new task
     *
     * @param _iTask, the task to push
     * @return the insertion success
     */
    public boolean pushTask(ITask _iTask) {
        boolean res = false;
        m_lockArray.lock();
        try {
            while (m_task.size() >= m_nbThreadMax + 1) {
                m_condPush.await();
            }
            res = m_task.add(_iTask);
            if (res) {
                m_condArray.signal();
                Logs.info("Add task : " + _iTask.getName(), true);
            }
        } catch (InterruptedException e) {
            Logs.warning("Unable to add task : " + _iTask.getName());
            Logs.exception(e);
        } finally {
            m_lockArray.unlock();
        }
        return res;
    }

    /**
     * Executor which get a task and execute it
     */
    final class Executor implements Runnable {

        final private int m_id;

        Executor(int _id) {
            m_id = _id;
        }

        @Override
        public void run() {

            while (true) {

                ITask todo = null;

                // Get task to do
                m_lockArray.lock();
                {
                    // Wait until they are data
                    while (m_task.isEmpty()) {
                        if (!isRunning()) {
                            m_lockArray.unlock();
                            return;
                        }
                        try {
                            m_condArray.await();
                        } catch (InterruptedException e) {
                            Logs.warning("Error while waiting for tasks : " + this.m_id);
                            Logs.exception(e);
                        }
                    }

                    // Choose a data
                    if (!m_task.isEmpty()) {
                        todo = m_task.removeFirst();
                    } else {
                        Logs.warning("Concurrency error in thread " + m_id);
                    }

                    // Log
                    if (todo != null) {
                        if (m_task.size() < m_nbThreadMax + 1) {
                            m_condPush.signalAll();
                        }
                        Logs.info("Task '" + todo.getName() + "' is chosen by thread " + m_id + " : remains " + m_task.size() + " task", true);
                    }

                }
                m_lockArray.unlock();

                if (todo != null) {

                    // Run task
                    try {
                        todo.run();
                    } catch (Throwable e) {
                        Logs.warning("Error catch in thread " + m_id + " : " + todo.getName());
                        Logs.exception(new Exception(e));
                    }

                    StringBuilder threadsInfos = new StringBuilder("[");
                    int remain;
                    int runnable = 0;
                    m_lockArray.lock();
                    {
                        for (Thread t : m_threads) {
                            threadsInfos.append(t.getState()).append(", ");
                            if (t.getState() == Thread.State.RUNNABLE || t.getState() == Thread.State.WAITING) {
                                ++runnable;
                            }
                        }
                        remain = m_task.size();
                    }
                    m_lockArray.unlock();
                    threadsInfos.append("]");

                    Logs.info("Task '" + todo.getName() + " from thread " + m_id + "  is finished : " + threadsInfos, true);
                    Logs.info("(" + todo.getName() + " : " + m_id + ") : remains " + remain + " task and " + runnable + " threads to wait", true);

                } else {
                    Logs.warning("No task to do by thread " + m_id);
                }
            }

        }
    }

}
