package com.wizbao.cloudpicture.utils;

/**
 * 线程工具类
 *
 */
public final class ThreadUtil {

    private ThreadUtil(){}

    /**
     * 获取 cpu 数量
     * @return cpu 数量
     * @since 2023.3.6
     */
    private static int cpuNum() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 最佳线程数量
     * @return 线程数量
     * @since 2023.3.6
     */
    public static int bestThreadNum() {
        int cpuNum = cpuNum();
        return cpuNum * 2;
    }

    /**
     * 最佳线程数量
     * 1. 如果目标值较小，则返回较少的即可。
     * @param targetSize 目标大小
     * @return 线程数量
     * @since 2023.3.6
     */
    public static int bestThreadNum(final int targetSize) {
        int bestNum = bestThreadNum();
        if(targetSize < bestNum) {
            return targetSize;
        }
        return bestNum;
    }

}
