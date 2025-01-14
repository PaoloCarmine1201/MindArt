package com.is.mindart.gestioneDisegno.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisegnoRepository extends JpaRepository<Disegno, Long> {

    /**
     * @param id id sessione
     * @return disegno
     */
    Optional<Disegno> findBySessioneId(Long id);

    /**
     * @param bambinoId id bambino
     * @return disegno
     */
    @SuppressWarnings("checkstyle:MethodName")
    List<Disegno> findByBambiniId(Long bambinoId);

    /**
     * Disegni da far valutare al modello
     * @return disegni da far valutare
     */
    List<Disegno> findAllByValutazioneEmotivaIsNullAndSessione_TerminataTrue();
}
