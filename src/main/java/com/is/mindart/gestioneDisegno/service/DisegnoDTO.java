package com.is.mindart.gestioneDisegno.service;


import com.is.mindart.gestioneDisegno.model.ValutazioneEmotiva;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisegnoDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Il campo sessione_id è obbligatorio")
    private Long sessioneId;

    @NotEmpty(message = "Almeno un bambino deve essere associato al disegno")
    private List<Long> bambinoIds;
}

