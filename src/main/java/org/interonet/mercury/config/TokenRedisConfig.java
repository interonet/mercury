package org.interonet.mercury.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class TokenRedisConfig {
    @Value("${spring.redis.token.host}")
    private String host;

    @Value("${spring.redis.token.port}")
    private int port;

    @Value("${spring.redis.token.database}")
    private int database;

    @Bean(name = "tokenRedisConnectionFactory")
    public RedisConnectionFactory tokenRedisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(host);
        redisConnectionFactory.setPort(port);
        redisConnectionFactory.setDatabase(database);
        return redisConnectionFactory;
    }

    @Bean(name = "tokenStringRedisTemplate")
    public StringRedisTemplate tokenRedisTemplate(@Qualifier("tokenRedisConnectionFactory") RedisConnectionFactory cf) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);
        return stringRedisTemplate;
    }
}