package com.is.mindart.gestioneCalendario.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRespository extends JpaRepository<Evento, Long> {
    List<Evento> findAllByTerapeutaId(Long treapeuta);
}
