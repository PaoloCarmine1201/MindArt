package com.is.mindart.gestioneDisegno.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisegnoRepository extends JpaRepository<Disegno, Long> {

    /**
     *
     * @param id
     * @return
     */
    Optional<Disegno> findBySessioneId(Long id);
}
