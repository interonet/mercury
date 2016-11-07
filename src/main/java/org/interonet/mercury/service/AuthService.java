package org.interonet.mercury.service;

import org.interonet.mercury.domain.auth.Credential;
import org.interonet.mercury.domain.auth.Token;
import org.interonet.mercury.domain.auth.User;
import org.interonet.mercury.domain.auth.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    @Autowired
    ConfigService configService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier("userRedisTemplate")
    private RedisTemplate<String, Token> userRedisTemplate;

    @Autowired
    @Qualifier("tokenRedisTemplate")
    private RedisTemplate<String, Token> tokenRedisTemplate;

    public boolean isTokenExisted(String token) {
        return tokenRedisTemplate.opsForValue().get(token) != null;
    }

    public User getUserByToken(String token) throws Exception {
        Token userToken = tokenRedisTemplate.opsForValue().get(token);
        if (userToken == null) {
            throw new Exception("Expire");
        }
        User user = userMapper.selectByUsername(userToken.getUsername());
        return user;
    }

    @Transactional
    public Token updateTokenByCredential(Credential credential) throws IOException {
        String username = credential.getUsername();
        String password = credential.getPassword();
        User user = userMapper.selectByUsernamePassword(username, password);
        if (user == null) {
            Token noAvailableToken = new Token(username, "", ZonedDateTime.now(), ZonedDateTime.now());
            return noAvailableToken;
        }

        Token old = userRedisTemplate.opsForValue().get(username);
        if (old != null) {
            userRedisTemplate.delete(username);
            tokenRedisTemplate.delete(old.getToken());
        }

        Token token = new Token();
        token.setUsername(username);
        token.setIssueAt(ZonedDateTime.now());
        Long tokenExpirePeriod = configService.getTokenExpirePeriod();
        token.setExpireAt(ZonedDateTime.now().plusMinutes(tokenExpirePeriod));

        userRedisTemplate.opsForValue().set(username, token);
        userRedisTemplate.expire(username, tokenExpirePeriod, TimeUnit.MINUTES);

        tokenRedisTemplate.opsForValue().set(token.getToken(), token);
        tokenRedisTemplate.expire(token.getToken(), tokenExpirePeriod, TimeUnit.MINUTES);

        return token;

    }

    @Scheduled(fixedRate = 5000)
    public void removeExpireToken() {
        //TODO: it can be done by redis automatically.
        return;
    }


}
