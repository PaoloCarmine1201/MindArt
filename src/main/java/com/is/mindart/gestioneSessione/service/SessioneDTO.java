package com.is.mindart.gestioneSessione.service;

import com.is.mindart.gestioneSessione.model.TipoSessione;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessioneDTO {

    @NotNull(message = "Il tipo di sessione non può essere vuoto")
    private TipoSessione tipoSessione;

    @NotNull(message = "Campo id terapeuta vuoto")
    private Long idTerapeuta;

    private String temaAssegnato;

    private Long materiale;

    @NotNull(message = "La lista di bambini non può essere vuota")
    private List<Long> bambini;


}
