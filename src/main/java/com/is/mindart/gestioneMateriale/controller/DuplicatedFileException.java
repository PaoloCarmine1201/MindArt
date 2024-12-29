package com.is.mindart.gestioneMateriale.controller;

/**
 * Eccezione lanciata quando si prova a salvare un file che già esiste nel database
 */
public class DuplicatedFileException extends RuntimeException {
    /**
     *
     * @param message messaggio
     */
    public DuplicatedFileException(final String message) {
        super(message);
    }
}
