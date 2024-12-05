package com.is.mindart.gestioneMateriale.model;

import lombok.ToString;

/**
 * Rappresenta i diversi tipi di materiale che possono essere associati a una sessione terapeutica.
 */
@ToString
public enum TipoMateriale {

    /**
     * Materiale di tipo PDF.
     */
    PDF,

    /**
     * Materiale di tipo immagine.
     */
    IMMAGINE,

    /**
     * Materiale di tipo video.
     */
    VIDEO
}
