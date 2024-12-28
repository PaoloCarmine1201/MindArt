package com.is.mindart.gestioneMateriale.model;

import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository per l'entità {@link Materiale}. Fornisce metodi di
 * accesso e manipolazione dei dati relativi ai materiali.
 */
public interface MaterialeRepository extends JpaRepository<Materiale, Long> {

    /**
     * Recupera la lista dei materiali filtrati per ID
     * del terapeuta.
     *
     * In JPA: SELECT m FROM Materiale m
     *         WHERE m.terapeuta.id = :terapeutaId
     *
     * @param terapeutaId ID del terapeuta
     * @return Lista di {@link Materiale} associati
     */
    List<Materiale> findByTerapeutaId(Long terapeutaId);

    /**
     * Verifica l'esistenza di un materiale in base al nome,
     * al tipo e al terapeuta.
     *
     * @param nome      Nome del materiale
     * @param tipo      Tipo del materiale
     * @param terapeuta Istanza di {@link Terapeuta} associata
     * @return true se esiste un materiale con le caratteristiche
     *         specificate; false altrimenti
     */
    boolean existsByNomeAndTipoAndTerapeuta(
            String nome,
            TipoMateriale tipo,
            Terapeuta terapeuta
    );
}
