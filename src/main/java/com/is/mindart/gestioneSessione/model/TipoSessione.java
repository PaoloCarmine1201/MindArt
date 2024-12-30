package com.is.mindart.gestioneSessione.model;

import lombok.ToString;

/**
 * Rappresenta i tipi di sessioni terapeutiche disponibili.
 */
@ToString
public enum TipoSessione {

    /**
     * Sessione dedicata al disegno.
     */
    DISEGNO,

    /**
     * Sessione dedicata all'uso del colore.
     */
    COLORE,

    /**
     * Sessione orientata all'apprendimento.
     */
    APPRENDIMENTO
}
