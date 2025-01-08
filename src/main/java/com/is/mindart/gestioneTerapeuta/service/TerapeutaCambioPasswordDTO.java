package com.is.mindart.gestioneTerapeuta.service;

import jakarta.validation.constraints.NotNull;
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
    private String newPassword;

}
