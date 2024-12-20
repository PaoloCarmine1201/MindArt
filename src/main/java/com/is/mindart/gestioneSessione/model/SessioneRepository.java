package com.is.mindart.gestioneSessione.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
/**
 * Repository per l'entità {@link Sessione}.
 * Fornisce operazioni CRUD e di query specifiche
 * per la gestione delle sessioni.
 */
import java.util.List;

public interface SessioneRepository extends JpaRepository<Sessione, Long> {
// il teraputa non npuò avviare una sessione se ne ha già una in corso
    /**
     * Restituisce tutte le sessioni non terminate di un bambino.
     * @param codiceBambino il codice del bambino
     * @return Lista delle sessioni
     */
     List<Sessione> findByTerminataFalseAndBambini_CodiceOrderByDataAsc(String codiceBambino);
    /**
     * Imposta il campo "terminata" della sessione.
     * @param id id della sessione
     * @return numero di record modificati.
     */
    @Modifying
    @Query("UPDATE Sessione s SET s.terminata = true WHERE s.id = :id")
    int terminaSessione(@Param("id") Long id);

}
