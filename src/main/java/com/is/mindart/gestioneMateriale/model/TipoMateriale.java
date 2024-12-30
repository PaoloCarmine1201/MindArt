package com.is.mindart.gestioneMateriale.model;

import lombok.ToString;

import java.io.Serializable;

/**
 * Rappresenta i diversi tipi di materiale disponibili.
 */
@ToString
public enum TipoMateriale implements Serializable {

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
