package com.is.mindart.gestioneTerapeuta.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneSessione.model.Sessione;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerapeutaDTOStat {

    @NotNull
    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String cognome;

    @NotNull
    private String email;

    @NotNull
    private String dataDiNascita;

    private int numeroSessioni = getNumeroSessioni();

    private int numeroBambini = getNumeroBambini();

}
