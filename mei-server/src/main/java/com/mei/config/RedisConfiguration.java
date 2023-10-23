package com.mei.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-23 14:39
 **/

@Configuration
@Slf4j
public class RedisConfiguration {
    @Bean
    public RedisTemplate redisTemplate(
            // 在starter中已经添加好了
            RedisConnectionFactory redisConnectionFactory
    ) {
        log.info("添加Redis依赖");
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
