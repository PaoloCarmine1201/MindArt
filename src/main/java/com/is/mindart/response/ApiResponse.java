package com.is.mindart.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Rappresenta una risposta standardizzata per le API.
 * Contiene un messaggio e, opzionalmente, dei dati.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    /**
     * Messaggio associato alla risposta.
     */
    private String message;

    /**
     * Dati opzionali inclusi nella risposta.
     */
    private Object data;
}
