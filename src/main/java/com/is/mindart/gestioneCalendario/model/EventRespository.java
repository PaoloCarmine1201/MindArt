package com.is.mindart.gestioneCalendario.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository per la gestione degli eventi del calendario.
 * Fornisce metodi standard e personalizzati per accedere ai dati.
 */
public interface EventRespository extends JpaRepository<Evento, Long> {

    /**
     * Trova tutti gli eventi associati a un terapeuta specifico.
     *
     * @param terapeutaId l'identificativo del terapeuta
     * @return una lista di eventi associati al terapeuta
     */
    List<Evento> findAllByTerapeutaId(Long terapeutaId);
}
