package com.is.mindart.gestioneSessione.model;

import lombok.ToString;

/**
 * Rappresenta i tipi di sessioni terapeutiche disponibili.
 */
@ToString
public enum TipoSessione {

    /**
     * Sessione di disegno.
     */
    DISEGNO,

    /**
     * Sessione di colore.
     */
    COLORE,

    /**
     * Sessione di apprendimento.
     */
    APPRENDIMENTO
}
