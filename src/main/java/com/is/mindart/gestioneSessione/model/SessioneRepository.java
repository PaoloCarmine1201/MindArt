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
    /**
     * Restituisce le sessioni non terminate
     * di un bambino ordinate per data.
     * @param codiceBambino codice del bambino
     */
    @SuppressWarnings("checkstyle:MethodName")
    List<Sessione> findByTerminataFalseAndBambini_CodiceOrderByDataAsc(String codiceBambino);

    /**
     * Restituisce le sessioni non terminate
     * di un terapeuta ordinate per data.
     * @param emailTerapeuta email del terapeuta
     */
    @SuppressWarnings("checkstyle:MethodName")
    List<Sessione> findByTerminataFalseAndTerapeuta_EmailOrderByDataAsc(String emailTerapeuta);
    /**
     * Imposta il campo "terminata" della sessione.
     * @param id id della sessione
     * @return numero di record modificati.
     */
    @Modifying
    @Query("UPDATE Sessione s SET s.terminata = true WHERE s.id = :id AND s.terminata = false")
    int terminaSessione(@Param("id") Long id);


}
