package com.is.mindart.gestioneSessione.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessioneRepository extends JpaRepository<Sessione, Long> {
// il teraputa non npuò avviare una sessione se ne ha già una in corso
    /**
     * Restituisce tutte le sessioni non terminate di un bambino.
     * @param codiceBambino il codice del bambino
     * @return Lista delle sessioni
     */
     List<Sessione> findByTerminataFalseAndBambini_CodiceOrderByDataAsc(String codiceBambino);
}
