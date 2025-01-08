package com.is.mindart.gestioneTerapeuta.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TerapeutaCambioPasswordDTO {
    /**
     * Identificativo univoco del terapeuta.
     */
    @NotNull
    private Long id;

    /**
     * Vecchia password del terapeuta.
     */
    @NotNull
    private String  oldPassword;

    /**
     * Nuova password del terapeuta.
     */
    @NotNull
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!?_.,:;@#$%^&*])[A-Za-z0-9!?_.,:;@#$%^&*]{8,}$",
            message = "La password deve contenere almeno una "
                    + "lettera maiuscola, un numero e un carattere speciale."
    )
    private String newPassword;

}
