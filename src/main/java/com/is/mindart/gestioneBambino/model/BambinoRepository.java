package com.is.mindart.gestioneBambino.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository per l'entità {@link Bambino}.
 * Fornisce operazioni CRUD e di query specifiche per la gestione dei terapeuti.
 */
public interface BambinoRepository extends JpaRepository<Bambino, Long> {
}
