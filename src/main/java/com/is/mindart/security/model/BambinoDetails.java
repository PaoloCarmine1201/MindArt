
package com.is.mindart.security.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class BambinoDetails implements UserDetails {

    private final Bambino bambino;
    private String tempPassword; // Qui potremmo usare il codice come password fittizia

    public BambinoDetails(Bambino bambino, String codice) {
        this.bambino = bambino;
        this.tempPassword = codice; // usiamo il codice come password "fittizia"
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        // Non c'è una password nel bambino, usiamo il codice come password fittizia
        return tempPassword;
    }

    @Override
    public String getUsername() {
        // username = codiceFiscale
        return bambino.getCodiceFiscale();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Bambino getBambino() {
        return bambino;
    }
}

