package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialeDTO {
    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private TipoMateriale tipoMateriale;

    @NotNull
    private Long terapeutaId;

//    @NotNull
//    private byte[] fileContent;
//
//    @NotNull
//    private String mimeType;
}
