package com.is.mindart.gestioneTerapeuta.service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TerapeutaCambioPasswordDTO {
    @NotNull
    Long id;
    @NotNull
    String oldPassword;
    @NotNull
    String newPassword;

}
