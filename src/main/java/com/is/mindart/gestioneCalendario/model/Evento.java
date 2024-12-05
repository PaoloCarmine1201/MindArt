package com.is.mindart.gestioneCalendario.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta un evento nel sistema di gestione del calendario.
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
     * Nome dell'evento.
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
     * Il terapeuta associato all'evento.
     */
    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    @JsonManagedReference
    private Terapeuta terapeuta;

}
