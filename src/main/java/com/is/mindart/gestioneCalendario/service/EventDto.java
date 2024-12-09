package com.is.mindart.gestioneCalendario.service;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class EventDto {
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
    private Long terapeuta;
}
