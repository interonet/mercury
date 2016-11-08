package org.interonet.mercury.config.redis;

import org.interonet.mercury.domain.auth.Token;
import org.interonet.mercury.util.JSR310ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class TokenRedisConfig {

    @Autowired
    JSR310ObjectMapper om;

    @Value("${spring.redis.token.host}")
    private String host;

    @Value("${spring.redis.token.port}")
    private int port;

    @Value("${spring.redis.token.database}")
    private int database;

    @Value("${spring.redis.token.password}")
    private String password;

    @Primary
    @Bean(name = "tokenRedisConnectionFactory")
    public RedisConnectionFactory tokenRedisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(host);
        redisConnectionFactory.setPort(port);
        redisConnectionFactory.setDatabase(database);
        redisConnectionFactory.setPassword(password);
        return redisConnectionFactory;
    }

    @Bean(name = "tokenRedisTemplate")
    public RedisTemplate<String, Token> userRedisTemplate(@Qualifier("tokenRedisConnectionFactory") RedisConnectionFactory rcf) {
        RedisTemplate<String, Token> template = new RedisTemplate<>();
        template.setConnectionFactory(rcf);
        Jackson2JsonRedisSerializer ser = new Jackson2JsonRedisSerializer<>(Token.class);
        ser.setObjectMapper(om);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(ser);
        return template;
    }
}