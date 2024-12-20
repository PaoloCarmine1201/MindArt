package com.is.mindart.security.model;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TerapeutaDetails implements UserDetails {

    private final Terapeuta terapeuta;

    public TerapeutaDetails(Terapeuta terapeuta) {
        this.terapeuta = terapeuta;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Potremmo assegnare ruoli o permessi in futuro, per ora vuoto.
        return null;
    }

    @Override
    public String getPassword() {
        return terapeuta.getPassword();
    }

    @Override
    public String getUsername() {
        return terapeuta.getEmail();
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

    public Terapeuta getTerapeuta() {
        return terapeuta;
    }
}
