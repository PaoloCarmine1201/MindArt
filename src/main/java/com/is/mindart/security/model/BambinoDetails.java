
package com.is.mindart.security.model;

import com.is.mindart.gestioneBambino.model.Bambino;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class BambinoDetails implements UserDetails {

    /**
     * -- GETTER --
     *  Metodo per ottenere il bambino
     *
     * @return il bambino
     */
    @Getter
    private final Bambino bambino;
    /**
     * -- GETTER --
     *  Metodo per ottenere la password temporanea
     *
     * @return la password temporanea
     */
    @Getter
    private String tempPassword; // Qui potremmo usare il codice come password fittizia

    public BambinoDetails(Bambino bambino, String codice) {
        this.bambino = bambino;
        this.tempPassword = codice; // usiamo il codice come password "fittizia"
    }


    /**
     * Metodo per ottenere la lista di ruoli dell'utente
     * @return la lista di ruoli
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_BAMBINO"));
    }

    /**
     * Metodo per ottenere la password dell'utente
     * @return la password
     */
    @Override
    public String getPassword() {
        // Non c'è una password nel bambino, usiamo il codice come password fittizia
        return tempPassword;
    }

    /**
     * Metodo per ottenere l'username dell'utente
     * @return l'username
     */
    @Override
    public String getUsername() {
        return bambino.getCodiceFiscale();
    }

    /**
     * Metodo per verificare se l'account dell'utente è scaduto
     * @return true se l'account non è scaduto, false altrimenti
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Metodo per verificare se l'account dell'utente è bloccato
     * @return true se l'account non è bloccato, false altrimenti
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Metodo per verificare se le credenziali dell'utente sono scadute
     * @return true se le credenziali non sono scadute, false altrimenti
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Metodo per verificare se l'utente è abilitato
     * @return true se l'utente è abilitato, false altrimenti
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}

