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
     * @param modelMapperParam -
     */
    @Autowired
    public MaterialeMapper(final ModelMapper modelMapperParam) {
        this.modelMapper = modelMapperParam;
    }

    /**
     * Post construct initialization.
     */
    @PostConstruct
    public void init() {
        configureMappings();
    }

    /**
     * Configura i mapping custom tra le classi.
     * - {@link Materiale} -> {@link OutputMaterialeDTO}
     * - {@link InputMaterialeDTO} -> {@link Materiale}
     */
    public void configureMappings() {
        // Mapping da Materiale -> OutputMaterialeDTO
        modelMapper.createTypeMap(Materiale.class, OutputMaterialeDTO.class)
                .addMappings(mapper -> {
                    mapper.map(Materiale::getId, OutputMaterialeDTO::setId);
                    mapper.map(Materiale::getNome, OutputMaterialeDTO::setNome);
                    mapper.map(Materiale::getTipo,
                            OutputMaterialeDTO::setTipoMateriale);
                });

        // Mapping da InputMaterialeDTO -> Materiale
        modelMapper.createTypeMap(InputMaterialeDTO.class, Materiale.class)
                .addMappings(mapper -> {
                    mapper.map(InputMaterialeDTO::getNome, Materiale::setNome);
                    mapper.map(InputMaterialeDTO::
                            getTipoMateriale, Materiale::setTipo);
                    // ID gestito dal database
                    mapper.skip(Materiale::setId);
                    // Il path è calcolato dal server
                    mapper.skip(Materiale::setPath);
                    // Non gestito nell'input
                    mapper.skip(Materiale::setSessioni);
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
     * @param path Path del file
     * @return {@link Materiale}
     */
    public Materiale toEntity(
            final InputMaterialeDTO inputMaterialeDTO,
            final String path) {
        Materiale materiale =
                modelMapper.map(inputMaterialeDTO, Materiale.class);
        materiale.setPath(path);
        return materiale;
    }
}
