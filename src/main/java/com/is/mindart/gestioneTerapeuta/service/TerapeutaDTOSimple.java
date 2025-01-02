package com.is.mindart.gestioneTerapeuta.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerapeutaDTOSimple {

    @NotNull(message = "L'ID non può essere nullo.")
    @NotBlank(message = "L'ID non può essere vuoto.")
    private Long id;

    @NotNull(message = "Il nome non può essere nullo.")
    @NotBlank(message = "Il nome non può essere vuoto.")
    private String nome;

    @NotNull(message = "Il cognome non può essere nullo.")
    @NotBlank(message = "Il cognome non può essere vuoto.")
    private String cognome;

    @NotNull(message = "L'email non può essere nulla.")
    @NotBlank(message = "L'email non può essere vuota.")
    @Email(message = "L'email deve essere valida.")
    private String email;
}
