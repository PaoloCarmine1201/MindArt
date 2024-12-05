package com.is.mindart.gestioneTerapeuta.service;

import lombok.Data;
import java.util.Date;

@Data
public class TerapeutaDtoEvento {
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private Date dataDiNascita;
}