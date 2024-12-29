package com.is.mindart.gestioneMateriale.controller;

/**
 * Eccezione lanciata se il file inviato è vuoto.
 */
public class EmptyFileException extends RuntimeException {
    /**
     *
     * @param message messaggio
     */
    public EmptyFileException(final String message) {
        super(message);
    }
}
