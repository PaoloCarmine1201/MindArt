package com.is.mindart.gestioneDisegno.service;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisegnoDTO {

    /**
     * L'identificativo del disegno.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * La valutazione emotiva associata al disegno.
     */
    @NotNull
    private List<StrokeDTO> strokes;

}

