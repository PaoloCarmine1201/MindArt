
package com.is.mindart.security.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.security.model.BambinoDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class BambinoUserDetailsService implements UserDetailsService {

    /**
     * Repository del bambino.
     */
    private BambinoRepository bambinoRepository;


    /**
     * Carica i dettagli del bambino tramite il codice.
     * @param codice il codice del bambino
     * @return un oggetto {@link BambinoDetails} rappresentante il bambino
     */
    public BambinoDetails loadBambinoByCodice(final String codice) {
        Bambino bambino = bambinoRepository.findByCodice(codice)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Bambino non trovato con : "
                                        + codice));

        // Verifica se il codice matcha quello del bambino
        if (!bambino.getCodice().equals(codice)) {
            throw new UsernameNotFoundException(
                    "Codice non valido per il bambino con CF: "
                            + codice);
        }

        return new BambinoDetails(bambino, codice);
    }

    /**
     * Carica i dettagli del bambino tramite il codice.
     * @param codice il codice del bambino
     * @return un oggetto {@link UserDetails} rappresentante il bambino
     * @throws UsernameNotFoundException se il bambino non è trovato
     */
    @Override
    public UserDetails loadUserByUsername(final String codice) throws UsernameNotFoundException {
        return loadBambinoByCodice(codice);
    }


}