package com.is.mindart.gestioneMateriale.controller;

/**
 * Eccezione lanciata quando il tipo di file non è supportato.
 */
public class UnsupportedFileException extends RuntimeException {
    /**
     *
     * @param message messaggio
     */
    public UnsupportedFileException(final String message) {
        super(message);
    }
}
