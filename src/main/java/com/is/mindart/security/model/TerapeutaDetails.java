
package com.is.mindart.security.model;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


@AllArgsConstructor
public class TerapeutaDetails implements UserDetails {

    /**
     * Terapeuta associato ad un token
     */
    private final Terapeuta terapeuta;


    /**
     * Restituisce l'autorità associata al terapeuta.
     * @return una lista di autorità
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_TERAPEUTA"));
    }

    /**
     * Restituisce la password del terapeuta.
     * @return la password del terapeuta
     */
    @Override
    public String getPassword() {
        return terapeuta.getPassword();
    }

    /**
     * Restituisce l'email del terapeuta.
     * @return l'email del terapeuta
     */
    @Override
    public String getUsername() {
        return terapeuta.getEmail();
    }

    /**
     * Restituisce se l'account del terapeuta è scaduto.
     * @return true se l'account non è scaduto, false altrimenti
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Restituisce se l'account del terapeuta è bloccato.
     * @return true se l'account non è bloccato, false altrimenti
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Restituisce se le credenziali del terapeuta sono scadute.
     * @return true se le credenziali non sono scadute, false altrimenti
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Restituisce se il terapeuta è abilitato.
     * @return true se il terapeuta è abilitato, false altrimenti
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Restituisce il terapeuta associato al token.
     * @return il terapeuta associato al token
     */
    public Terapeuta getTerapeuta() {
        return terapeuta;
    }
}
