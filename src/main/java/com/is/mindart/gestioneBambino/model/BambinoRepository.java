package com.is.mindart.gestioneBambino.model;

import com.is.mindart.gestioneCalendario.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository per l'entità {@link Bambino}.
 * Fornisce operazioni CRUD e di query specifiche per la gestione dei terapeuti.
 */
public interface BambinoRepository extends JpaRepository<Bambino, Long> {

    /**
     * Trova tutti i bambini associati ad un terapeuta.
     *
     * @param terapeutaId l'identificativo del terapeuta
     * @return una lista di bambini associati al terapeuta
     */
    List<Bambino> findAllByTerapeutaId(Long terapeutaId);
}
