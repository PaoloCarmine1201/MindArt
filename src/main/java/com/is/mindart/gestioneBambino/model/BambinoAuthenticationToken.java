package com.is.mindart.gestioneBambino.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class BambinoAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;   // codiceFiscale
    private Object credentials;       // codiceSegreto

    public BambinoAuthenticationToken(String codiceFiscale, String codiceSegreto) {
        super(null);
        this.principal = codiceFiscale;
        this.credentials = codiceSegreto;
        setAuthenticated(false);
    }

    public BambinoAuthenticationToken(UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.principal = userDetails.getUsername();
        this.credentials = userDetails.getPassword(); // non necessario mantenerla
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
