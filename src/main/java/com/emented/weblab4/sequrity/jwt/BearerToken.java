package com.emented.weblab4.sequrity.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;

public class BearerToken extends AbstractAuthenticationToken {

    private final BearerUser bearerUser;

    public BearerToken(BearerUser bearerUser) {
        super(new ArrayList<>());
        this.bearerUser = bearerUser;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return bearerUser;
    }
}
