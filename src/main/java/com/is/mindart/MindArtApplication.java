package com.is.mindart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MindArtApplication {

    protected MindArtApplication() {
    }

    /**
     * Metodo principale per l'avvio dell'applicazione.
     *
     * @param args gli argomenti passati all'applicazione
     */
    public static void main(final String[] args) {
        SpringApplication.run(MindArtApplication.class, args);
    }

}
