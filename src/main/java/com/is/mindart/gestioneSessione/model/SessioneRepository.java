package com.is.mindart.gestioneSessione.model;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per l'entità {@link Sessione}.
 * Fornisce operazioni CRUD e di query specifiche per la gestione delle sessioni.
 */
public interface SessioneRepository extends JpaRepository<Sessione, Long> {
}
