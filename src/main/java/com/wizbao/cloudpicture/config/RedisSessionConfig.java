package com.wizbao.cloudpicture.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @description 由于spring session写入redis时，value值默认使用JdkSerializationRedisSerializer，以二进制形式存储，占用空间大且不利于查看。
 * 于是配置 Spring Session 写入 Redis 时的序列化方式
 * @author Weber
 * @since 2026-10-26
 */
@Configuration
public class RedisSessionConfig {

    /**
     * Session 的 Redis key、hash key 由 Spring Session 固定使用 StringRedisSerializer。
     * hash value 不能再用 StringRedisSerializer：其中有 creationTime（Long）、
     * maxInactiveInterval（Integer）以及登录用户对象。这里改成 JSON 文本。
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return createSerializer();
    }

    static RedisSerializer<Object> createSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(mapper, null);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
}
