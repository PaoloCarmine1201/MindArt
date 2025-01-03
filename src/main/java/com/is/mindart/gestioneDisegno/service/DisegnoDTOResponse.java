package com.is.mindart.gestioneDisegno.service;

import com.is.mindart.gestioneDisegno.model.ValutazioneEmotiva;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisegnoDTOResponse {

    /**
     * L'identificativo del disegno.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tema assegnato al disegno.
     */
    @NotNull
    private String tema;

    /**
     * Data di creazione del disegno.
     */
    @NotNull
    private LocalDateTime data;

    /**
     * Voto assegnato al disegno.
     */
    private int voto;

    /**
     * Valutazione emotiva associata al disegno.
     */
    private ValutazioneEmotiva valutazioneEmotiva;

}
