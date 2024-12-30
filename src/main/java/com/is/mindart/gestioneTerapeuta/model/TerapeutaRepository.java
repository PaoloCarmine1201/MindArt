package com.is.mindart.gestioneTerapeuta.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository per l'entità {@link Terapeuta}.
 * Fornisce operazioni CRUD e di query specifiche per la gestione dei terapeuti.
 */
public interface TerapeutaRepository extends JpaRepository<Terapeuta, Long> {
    /**
     * Restituisce il terapeuta con l'email specificata.
     * @param email l'email del terapeuta
     * @return il terapeuta con l'email specificata
     */
    Optional<Terapeuta> findByEmail(String email);
}
