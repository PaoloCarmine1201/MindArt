package com.is.mindart.gestioneTerapeuta.service;

import lombok.Data;
import java.util.Date;

@Data
public class TerapeutaEventoDTO {
    /**
     * Identificativo univoco del terapeuta.
     */
    private Long id;

    /**
     * Nome del terapeuta.
     */
    private String nome;

    /**
     * Cognome del terapeuta.
     */
    private String cognome;

    /**
     * Email del terapeuta.
     */
    private String email;

    /**
     * Data di nascita del terapeuta.
     */
    private Date dataDiNascita;

    /**
     * Password del terapeuta per l'accesso al sistema.
     */
    private String password;
}