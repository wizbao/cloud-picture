package com.wizbao.cloudpicture.manager.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class PictureShardingAlgorithm implements StandardShardingAlgorithm<Long> {

    /**
     * 按空间 id 精确路由到图片分表
     *
     * @param availableTargetNames 可用目标表
     * @param preciseShardingValue 精确分片值
     * @return 实际表名
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> preciseShardingValue) {
        Long spaceId = preciseShardingValue.getValue();
        String logicTableName = preciseShardingValue.getLogicTableName();
        // spaceId 为 null 表示查询所有图片
        if (spaceId == null) {
            return logicTableName;
        }
        // 根据 spaceId 动态生成分表名
        String realTableName = "picture_" + spaceId;
        if (availableTargetNames.contains(realTableName)) {
            return realTableName;
        } else {
            return logicTableName;
        }
    }

    /**
     * 范围分片，当前未使用
     *
     * @param collection 可用目标表
     * @param rangeShardingValue 范围分片值
     * @return 目标表集合
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        return new ArrayList<>();
    }

    /**
     * 获取算法配置
     *
     * @return 算法配置
     */
    @Override
    public Properties getProps() {
        return null;
    }

    /**
     * 初始化分片算法
     *
     * @param properties 算法配置
     */
    @Override
    public void init(Properties properties) {

    }
}
