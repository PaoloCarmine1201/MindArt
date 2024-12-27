package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.gestioneMateriale.model.TipoMateriale;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputMaterialeDTO {
    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private TipoMateriale tipoMateriale;

    @NotNull
    private Long terapeutaId;

    private MultipartFile file;

    public InputMaterialeDTO(Long id, String nome, TipoMateriale tipoMateriale, Long terapeutaId) {
        this.id = id;
        this.nome = nome;
        this.tipoMateriale = tipoMateriale;
        this.terapeutaId = terapeutaId;
    }

    public InputMaterialeDTO(String nome, TipoMateriale tipoMateriale, Long terapeutaId, MultipartFile file) {
        this.nome = nome;
        this.tipoMateriale = tipoMateriale;
        this.terapeutaId = terapeutaId;
        this.file = file;
    }
}
