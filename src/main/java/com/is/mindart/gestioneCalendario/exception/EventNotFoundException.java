package com.is.mindart.gestioneCalendario.exception;

public class EventNotFoundException extends RuntimeException {
    /**
     * @param message messaggio
     */
    public EventNotFoundException(final String message) {
        super(message);
    }
}
