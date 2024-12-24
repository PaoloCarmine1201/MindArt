package com.is.mindart.gestioneCalendario.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per la gestione degli eventi del calendario.
 * Fornisce metodi standard e personalizzati per accedere ai dati.
 */
public interface EventoRespository extends JpaRepository<Evento, Long> {

    /**
     * Trova tutti gli eventi associati a un terapeuta specifico.
     *
     * @param terapeutaId l'identificativo del terapeuta
     * @return una lista di eventi associati al terapeuta
     */
    List<Evento> findAllByTerapeutaId(Long terapeutaId);

    /**
     * Trova un evento specifico tramite il suo ID e l'ID del terapeuta.
     * @param id l'identificativo dell'evento
     * @param terapeutaId l'identificativo del terapeuta
     * @return un oggetto {@link Evento} rappresentante l'evento
     */
    Optional<Evento> findByIdAndTerapeutaId(Long id, Long terapeutaId);
}
