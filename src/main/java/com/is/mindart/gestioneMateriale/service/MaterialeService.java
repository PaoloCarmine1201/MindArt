package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.configuration.MaterialeMapper;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialeService {

    /**
     * Repository relativa al materiale.
     */
    private final MaterialeRepository materialeRepository;
    /**
     * Custom mapper per il materiale.
     */
    private final MaterialeMapper materialeMapper;

    /**
     * Costruttore.
     * @param materialeRepository {@link MaterialeRepository}
     * @param materialeMapper {@link MaterialeMapper}
     */
    @Autowired
    public MaterialeService(final MaterialeRepository materialeRepository,
                            final MaterialeMapper materialeMapper) {
        this.materialeRepository = materialeRepository;
        this.materialeMapper = materialeMapper;
    }

    /**
     * Trova tutti i materiali associati al terapeutae li converte in
     * {@link GetMaterialeDTO} per la visualizzazione sul client.
     * @param terapeutaId - id del terapeuta loggato
     * @return listaDTO
     */
    public List<GetMaterialeDTO> getClientMateriale(final long terapeutaId) {
        return materialeRepository.findByTerapeutaId(terapeutaId)
                .stream()
                .map(materialeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
