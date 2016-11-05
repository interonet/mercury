package org.interonet.mercury.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.interonet.mercury.domain.auth.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class UserRedisConfig {
    @Value("${spring.redis.user.host}")
    private String host;

    @Value("${spring.redis.user.port}")
    private int port;

    @Value("${spring.redis.user.database}")
    private int database;

    @Primary
    @Bean(name = "userRedisConnectionFactory")
    public RedisConnectionFactory userRedisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
        redisConnectionFactory.setHostName(host);
        redisConnectionFactory.setPort(port);
        redisConnectionFactory.setDatabase(database);
        return redisConnectionFactory;
    }

    @Bean(name = "userRedisTemplate")
    public RedisTemplate<String, Token> userRedisTemplate(@Qualifier("userRedisConnectionFactory") RedisConnectionFactory rcf) {
        RedisTemplate<String, Token> template = new RedisTemplate<>();
        template.setConnectionFactory(rcf);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JsonRedisSerializer(Token.class));
        return template;
    }

    static class JsonRedisSerializer implements RedisSerializer<Object> {
        private final ObjectMapper om;
        private final Class clazz;

        public JsonRedisSerializer(Class clazz) {
            this.clazz = clazz;
            this.om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }

        @Override
        public byte[] serialize(Object t) throws SerializationException {
            try {
                return om.writeValueAsBytes(t);
            } catch (JsonProcessingException e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null) {
                return null;
            }
            try {
                return om.readValue(bytes, clazz);
            } catch (Exception e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }
    }

}