package com.is.mindart.gestioneBambino.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

/**
 * Repository per l'entità {@link Bambino}.
 * Fornisce operazioni CRUD e di query specifiche per la gestione dei terapeuti.
 */
public interface BambinoRepository extends JpaRepository<Bambino, Long> {

    /**
     * Trova tutti i bambini associati ad un terapeuta.
     *
     * @param terapeutaId l'identificativo del terapeuta
     * @return una lista di bambini associati al terapeuta
     */
    List<Bambino> findAllByTerapeutaId(Long terapeutaId);
    /**
     * Restituisce il bambino con il codice specificato.
     * @param codice il codice del bambino
     * @return il bambino con il codice specificato
     */
    Optional<Bambino> findByCodice(String codice);
    /**
     * Restituisce il bambino con il codice fiscale specificato.
     * @param codiceFiscale il codice fiscale del bambino
     * @return il bambino con il codice fiscale specificato
     */
    Optional<Bambino> findByCodiceFiscale(String codiceFiscale);

}
