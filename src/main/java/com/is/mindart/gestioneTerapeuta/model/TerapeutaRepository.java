package com.is.mindart.gestioneTerapeuta.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository per l'entità {@link Terapeuta}.
 * Fornisce operazioni CRUD e di query specifiche per la gestione dei terapeuti.
 */
public interface TerapeutaRepository extends JpaRepository<Terapeuta, Long> {
}
