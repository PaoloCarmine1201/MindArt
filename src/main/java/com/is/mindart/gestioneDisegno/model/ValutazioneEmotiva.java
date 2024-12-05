package com.is.mindart.gestioneDisegno.model;

import lombok.ToString;

/**
 * Rappresenta le valutazioni emotive che possono essere associate a un disegno.
 */
@ToString
public enum ValutazioneEmotiva {

    /**
     * Indica che l'emozione valutata è tristezza.
     */
    TRISTEZZA,

    /**
     * Indica che l'emozione valutata è felicità.
     */
    FELICITA,

    /**
     * Indica che l'emozione valutata è rabbia.
     */
    RABBIA,

    /**
     * Indica che l'emozione valutata è paura.
     */
    PAURA
}
