
package com.is.mindart.security.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.security.model.BambinoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BambinoUserDetailsService {

    @Autowired
    private BambinoRepository bambinoRepository;

    public BambinoDetails loadBambinoByCodiceFiscaleECodice(
            String codiceFiscale, String codice) {
        Bambino bambino = bambinoRepository.findByCodiceFiscale(codiceFiscale)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Bambino non trovato con CF: "
                                        + codiceFiscale));

        // Verifica se il codice matcha quello del bambino
        if(!bambino.getCodice().equals(codice)){
            throw new UsernameNotFoundException(
                    "Codice non valido per il bambino con CF: "
                            + codiceFiscale);
        }

        return new BambinoDetails(bambino, codice);
    }

    public BambinoDetails loadBambinoByCodice(String codice) {
        Bambino bambino = bambinoRepository.findByCodice(codice)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Bambino non trovato con : "
                                        + codice));

        // Verifica se il codice matcha quello del bambino
        if(!bambino.getCodice().equals(codice)){
            throw new UsernameNotFoundException(
                    "Codice non valido per il bambino con CF: "
                            + codice);
        }

        return new BambinoDetails(bambino, codice);
    }
}