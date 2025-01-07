package com.is.mindart.gestioneMateriale.controller;

/**
 * Eccezione lanciata quando il tipo di file è nullo o vuoto.
 */
public class FileTypeNullException extends RuntimeException {
    /**
     *
     * @param message messaggio
     */
    public FileTypeNullException(final String message) {
        super(message);
    }
}
