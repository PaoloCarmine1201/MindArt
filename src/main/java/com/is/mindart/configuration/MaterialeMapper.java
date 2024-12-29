package com.is.mindart.configuration;

import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.service.InputMaterialeDTO;
import com.is.mindart.gestioneMateriale.service.OutputMaterialeDTO;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaterialeMapper {

    /**
     * Istanza iniettata del modelMapper.
     */
    private final ModelMapper modelMapper;

    /**
     * Costruttore.
     * @param modelMapper -
     */
    @Autowired
    public MaterialeMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Post construct initialization.
     */
    @PostConstruct
    public void init() {
        configureMappings();
    }

    /**
     * Configura i mapping custom tra le classi:
     * - {@link Materiale} -> {@link OutputMaterialeDTO}
     * - {@link InputMaterialeDTO} -> {@link Materiale}
     */
    public void configureMappings() {
        // Mapping da Materiale -> OutputMaterialeDTO
        modelMapper.createTypeMap(Materiale.class, OutputMaterialeDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Materiale::getId, OutputMaterialeDTO::setId);
                    mapper.map(Materiale::getNome, OutputMaterialeDTO::setNome);
                    mapper.map(Materiale::getTipo, OutputMaterialeDTO::setTipoMateriale);
                });

        // Mapping da InputMaterialeDTO -> Materiale
        modelMapper.createTypeMap(InputMaterialeDTO.class, Materiale.class)
                .addMappings(mapper -> {
                    mapper.map(InputMaterialeDTO::getNome, Materiale::setNome);
                    mapper.map(InputMaterialeDTO::getTipoMateriale, Materiale::setTipo);
                    mapper.skip(Materiale::setId); // ID gestito dal database
                    mapper.skip(Materiale::setPath); // Il path è calcolato dal server
                    mapper.skip(Materiale::setSessioni); // Non gestito nell'input
                });
    }

    /**
     * Converte {@link Materiale} in {@link OutputMaterialeDTO}.
     * @param materiale {@link Materiale}
     * @return {@link OutputMaterialeDTO}
     */
    public OutputMaterialeDTO toDTO(final Materiale materiale) {
        return modelMapper.map(materiale, OutputMaterialeDTO.class);
    }

    /**
     * Converte {@link InputMaterialeDTO} in {@link Materiale}.
     * @param inputMaterialeDTO {@link InputMaterialeDTO}
     * @return {@link Materiale}
     */
    public Materiale toEntity(final InputMaterialeDTO inputMaterialeDTO, String path) {
        Materiale materiale = modelMapper.map(inputMaterialeDTO, Materiale.class);
        materiale.setPath(path);
        return materiale;
    }
}
