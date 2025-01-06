package com.is.mindart.gestioneDisegno.service;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValutazioneRequest {
    /**
     * Valutazione assegnata al disegno.
     */
    @Max(10)
    @Min(0)
    private Integer valutazione;
}
