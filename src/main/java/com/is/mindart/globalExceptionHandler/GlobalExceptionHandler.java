package com.is.mindart.globalExceptionHandler;

import com.is.mindart.gestioneMateriale.controller.DuplicatedFileException;
import com.is.mindart.gestioneMateriale.controller.EmptyFileException;
import com.is.mindart.gestioneMateriale.controller.UnsupportedFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce l'eccezione EmptyFileException.
     * @param ex eccezione EmptyFileException.
     * @return ResponseEntity con messaggio di errore.
     */
    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<String> handleEmptyFileException(
            final EmptyFileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Gestisce l'eccezione UnsupportedFileException.
     * @param ex eccezione UnsupportedFileException.
     * @return ResponseEntity con messaggio di errore.
     */
    @ExceptionHandler(UnsupportedFileException.class)
    public ResponseEntity<String> handleUnsupportedFileException(
            final UnsupportedFileException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Gestisce l'eccezione DuplicatedFileException.
     * @param ex eccezione DuplicatedFileException.
     * @return ResponseEntity con messaggio di errore.
     */
    @ExceptionHandler(DuplicatedFileException.class)
    public ResponseEntity<String> handleDuplicatedFileException(
            final DuplicatedFileException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    /**
     * Gestisce l'eccezione generica.
     * @return ResponseEntity con messaggio di errore.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Errore interno del server");
    }
}
