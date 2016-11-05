package org.interonet.mercury.domain.auth;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Token {
    String token;
    ZonedDateTime issueAt;
    ZonedDateTime expireAt;

    public Token() {
        token = UUID.randomUUID().toString();
    }

    public Token(String token, ZonedDateTime issueAt, ZonedDateTime expireAt) {
        this.token = token;
        this.issueAt = issueAt;
        this.expireAt = expireAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getIssueAt() {
        return issueAt;
    }

    public void setIssueAt(ZonedDateTime issueAt) {
        this.issueAt = issueAt;
    }

    public ZonedDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
    }
}
