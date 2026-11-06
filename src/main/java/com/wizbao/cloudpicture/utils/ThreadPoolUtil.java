package com.wizbao.cloudpicture.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 线程池帮助类
 *
 */
public class ThreadPoolUtil {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolUtil.class);

    /**
     * 默认线程空闲存活时间，单位毫秒
     */
    public static final long DEF_KEEP_ALIVE_TIME = 30000L;

    /**
     * 创建固定大小、无缓冲队列的线程池
     *
     * @param nThreads 线程数
     * @param keepAliveTime 空闲存活时间，单位毫秒
     * @return 线程池
     */
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

    /**
     * 创建固定大小、有界队列的线程池
     *
     * @param nThreads 线程数
     * @param capacity 队列容量
     * @param keepAliveTime 空闲存活时间，单位毫秒
     * @return 线程池
     */
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

    /**
     * 按 CPU 核数创建默认固定线程池
     *
     * @return 线程池
     */
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

    /**
     * 按 CPU 核数创建指定队列容量的固定线程池
     *
     * @param capacity 队列容量
     * @return 线程池
     */
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

    /**
     * 按 CPU 核数创建指定存活时间的固定线程池
     *
     * @param keepAliveTime 空闲存活时间，单位毫秒
     * @return 线程池
     */
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

    /**
     * 创建固定大小的守护线程池
     *
     * @param nThreads 线程数
     * @param keepAliveTime 空闲存活时间，单位毫秒
     * @return 线程池
     */
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


    /**
     * 创建单线程执行器
     *
     * @param keepAliveTime 空闲存活时间，单位毫秒
     * @return 线程池
     */
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

    /**
     * 创建单线程守护执行器
     *
     * @param keepAliveTime 空闲存活时间，单位毫秒
     * @return 线程池
     */
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
        /**
         * 守护线程工厂
         */
        public static final ThreadFactory daemonThreadFactory = new DaemonThreadFactory();

        /**
         * 创建守护线程
         *
         * @param r 任务
         * @return 守护线程
         */
        @Override
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        }
    }

    /**
     * 按 CPU 核数创建定时任务线程池
     *
     * @return 定时任务线程池
     */
    public static ScheduledThreadPoolExecutor newDefScheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(ThreadUtil.bestThreadNum());
    }

    /**
     * 创建指定线程数的定时任务线程池
     *
     * @param nThreads 线程数
     * @return 定时任务线程池
     */
    public static ScheduledThreadPoolExecutor newScheduledThreadPoolExecutor(int nThreads) {
        return new ScheduledThreadPoolExecutor(nThreads);
    }
}
