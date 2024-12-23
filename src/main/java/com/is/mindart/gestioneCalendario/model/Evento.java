package com.is.mindart.gestioneCalendario.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Rappresenta un evento nel calendario associato a un terapeuta.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Evento {
    /**
     * Identificativo univoco dell'evento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome o descrizione dell'evento.
     */
    private String nome;

    /**
     * Data e ora di inizio dell'evento.
     */
    private Date inizio;

    /**
     * Data e ora di fine dell'evento.
     */
    private Date fine;

    /**
     * Terapeuta associato all'evento.
     * La relazione è gestita con {@link JsonManagedReference} per evitare cicli
     * infiniti nella serializzazione JSON.
     */
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    @JsonManagedReference
    private Terapeuta terapeuta;

}
