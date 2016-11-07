package org.interonet.mercury;

import org.interonet.mercury.domain.auth.Credential;
import org.interonet.mercury.domain.auth.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getTokenByCorrectCredential() {
        Credential credential = new Credential("admin", "admin");
        ResponseEntity<Token> responseEntity = restTemplate.postForEntity("/auth/token", credential, Token.class);
        Token token = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(token);
        assertEquals(token.getUsername(), credential.getUsername());

        assertNotNull(token.getToken());
        assertNotEquals(token.getToken(), "");
    }

    @Test
    public void getTokenByCorrectWrongCredential() {
        Credential credential = new Credential("admin", "root");
        ResponseEntity<Token> responseEntity = restTemplate.postForEntity("/auth/token", credential, Token.class);
        Token token = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(token);
        assertEquals(token.getUsername(), credential.getUsername());

        assertEquals(token.getToken(), "");
    }
}
