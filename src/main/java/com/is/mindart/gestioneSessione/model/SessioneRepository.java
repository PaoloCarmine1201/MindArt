package com.is.mindart.gestioneSessione.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository per l'entità {@link Sessione}.
 * Fornisce operazioni CRUD e di query specifiche
 * per la gestione delle sessioni.
 */
public interface SessioneRepository extends JpaRepository<Sessione, Long> {
    /**
     * Imposta il campo "terminata" della sessione.
     * @param id id della sessione
     * @return numero di record modificati.
     */
    @Modifying
    @Query("UPDATE Sessione s SET s.terminata = true WHERE s.id = :id")
    int terminaSessione(@Param("id") Long id);
}
