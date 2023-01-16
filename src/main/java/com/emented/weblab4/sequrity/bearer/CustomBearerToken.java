package com.emented.weblab4.sequrity.bearer;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.ArrayList;

public class CustomBearerToken extends AbstractAuthenticationToken {

    private final CustomBearerUser customBearerUser;

    public CustomBearerToken(CustomBearerUser customBearerUser) {
        super(new ArrayList<>());
        this.customBearerUser = customBearerUser;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return customBearerUser;
    }
}
