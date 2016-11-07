package org.interonet.mercury.controller;

import org.interonet.mercury.domain.auth.Credential;
import org.interonet.mercury.domain.auth.Token;
import org.interonet.mercury.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@EnableAutoConfiguration
public class AuthController {
    @Autowired
    AuthService authService;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public Token postUser(@RequestBody Credential credential) throws Exception {
        return authService.updateTokenByCredential(credential);
    }
}