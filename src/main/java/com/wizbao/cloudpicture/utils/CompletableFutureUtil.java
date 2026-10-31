package com.wizbao.cloudpicture.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

/**
 * 异步编排线程工具类
 */
public class CompletableFutureUtil {
    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureUtil.class);

    /**
     * 执行异步编程
     *
     * 收集异步线程的结果,线程耗费不大,使用系统默认的ForkJionPool
     *
     * @param completableFutures
     * @param <T>
     * @return
     */
    public static <T> List<T> execCompletableFutureList(CompletableFuture<T>[] completableFutures){
        try {
            CompletableFuture<List<T>> batchFuture = CompletableFuture.allOf(completableFutures).thenApplyAsync( r -> {
                List<T> allResult = new ArrayList<>();
                for (CompletableFuture<T> completableFuture : completableFutures) {
                    try {
                        T t = completableFuture.get();
                        allResult.add(t);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                return allResult;
            });
            return batchFuture.get();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 执行异步编程
     *
     * @param function   执行方法
     * @param paramList  执行方法参数
     * <br>执行方法4个参数以上的 封装成传值对象传递
     *
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> execCompletableFutureList(Function<T, R> function, List<T> paramList) {
        CompletableFuture<R>[] execFutures = new CompletableFuture[paramList.size()];
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.newDefFixedThreadPool(paramList.size());
        try {
            for (int index = 0; index < paramList.size(); index++) {
                final T param = paramList.get(index);
                CompletableFuture<R> execfuture = CompletableFuture.supplyAsync(() -> function.apply(param), threadPoolExecutor);
                execFutures[index] = execfuture;
            }
            return execCompletableFutureList(execFutures);
        } finally {
            threadPoolExecutor.shutdown();
        }
    }

    /**
     * 执行异步编程
     *
     * @param function   执行方法
     * @param paramList  执行方法参数
     * <br>执行方法4个参数以上的 封装成传值对象传递
     *
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T , R> List<R> execCompletableFutureList(Function<T , R> function , List<T> paramList , int threadPoolCapacity){
        CompletableFuture<R>[] execFutures = new CompletableFuture[paramList.size()];
        for(int index = 0 ; index < paramList.size() ; index++){
            final T param = paramList.get(index);
            CompletableFuture<R> execfuture =  CompletableFuture.supplyAsync(() -> function.apply(param) , ThreadPoolUtil.newDefFixedThreadPool(threadPoolCapacity));
            execFutures[index] = execfuture;
        }

        return execCompletableFutureList(execFutures);
    }
}