package com.is.mindart.gestioneMateriale.service;

import com.is.mindart.configuration.MaterialeMapper;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import jakarta.persistence.EntityNotFoundException;
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
     * Repository relativa al terapeuta.
     */
    private final TerapeutaRepository terapeutaRepository;

    /**
     * Costruttore.
     * @param materialeRepository {@link MaterialeRepository}
     * @param materialeMapper {@link MaterialeMapper}
     */
    @Autowired
    public MaterialeService(final MaterialeRepository materialeRepository,
                            final MaterialeMapper materialeMapper,
                            final TerapeutaRepository terapeutaRepository) {
        this.materialeRepository = materialeRepository;
        this.materialeMapper = materialeMapper;
        this.terapeutaRepository = terapeutaRepository;
    }

    /**
     * Restituisce i materiali caricati da un terapeuta.
     * @param email l'email del terapeuta
     * @return la lista dei materiali caricati dal terapeuta
     */
    public List<GetMaterialeDTO> getClientMateriale(final String email) {
        Terapeuta terapeuta = terapeutaRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Terapeuta non trovato"));
        return materialeRepository.findByTerapeutaId(terapeuta.getId()).stream()
                .map(materialeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
