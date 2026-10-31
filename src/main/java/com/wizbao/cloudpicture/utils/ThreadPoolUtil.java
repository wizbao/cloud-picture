package com.wizbao.cloudpicture.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 线程池帮助类
 *
 */
public class ThreadPoolUtil {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolUtil.class);

    public static final long DEF_KEEP_ALIVE_TIME = 30000L;

    public static ThreadPoolExecutor newFixedThreadPool(int nThreads, long keepAliveTime) {
        return new ThreadPoolExecutor(nThreads, nThreads, keepAliveTime, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }

    public static ThreadPoolExecutor newFixedThreadPool(int nThreads, int capacity , long keepAliveTime) {
        return new ThreadPoolExecutor(nThreads, nThreads, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(capacity), (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }

    public static ThreadPoolExecutor newDefFixedThreadPool() {
        int nThreads = Runtime.getRuntime().availableProcessors() + 1;
        return new ThreadPoolExecutor(nThreads, nThreads, DEF_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(500), (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }

    public static ThreadPoolExecutor newDefFixedThreadPool(int capacity) {
        int nThreads = Runtime.getRuntime().availableProcessors() + 1;
        return new ThreadPoolExecutor(nThreads, nThreads, DEF_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(capacity), (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }

    public static ThreadPoolExecutor newDefFixedThreadPool(long keepAliveTime) {
        int nThreads = Runtime.getRuntime().availableProcessors() + 1;
        return new ThreadPoolExecutor(nThreads, nThreads, keepAliveTime, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }

    public static ThreadPoolExecutor newFixedDaemonThreadPool(int nThreads, long keepAliveTime) {
        return new ThreadPoolExecutor(nThreads, nThreads, keepAliveTime, TimeUnit.MILLISECONDS, new SynchronousQueue<>(),
                DaemonThreadFactory.daemonThreadFactory, (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }


    public static ThreadPoolExecutor newSingleThreadExecutor(long keepAliveTime) {
        return new ThreadPoolExecutor(1, 1, keepAliveTime, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }

    public static ThreadPoolExecutor newSingleDaemonThreadExecutor(long keepAliveTime) {
        return new ThreadPoolExecutor(1, 1, keepAliveTime, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), DaemonThreadFactory.daemonThreadFactory
                , (r, exe) -> {
            if (!exe.isShutdown()) {
                try {
                    exe.getQueue().put(r);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        });
    }

    static class DaemonThreadFactory implements ThreadFactory {
        public static final ThreadFactory daemonThreadFactory = new DaemonThreadFactory();

        @Override
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        }
    }

    /**
     * 定时任务线程池
     *
     * @return
     */
    public static ScheduledThreadPoolExecutor newDefScheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(ThreadUtil.bestThreadNum());
    }

    /**
     * 定时任务线程池
     *
     * @return
     */
    public static ScheduledThreadPoolExecutor newScheduledThreadPoolExecutor(int nThreads) {
        return new ScheduledThreadPoolExecutor(nThreads);
    }
}
