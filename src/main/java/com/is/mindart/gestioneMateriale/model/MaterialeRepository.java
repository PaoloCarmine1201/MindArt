package com.is.mindart.gestioneMateriale.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialeRepository extends JpaRepository<Materiale, Long> {
    /**
     * In JPA: select m from Materiale m where m.terapeuta.id = :terapeutaId.
     * @param terapeutaId - id del terapeuta
     * @return lista di materiali associati
     */
    List<Materiale> findByTerapeutaId(Long terapeutaId);
}
