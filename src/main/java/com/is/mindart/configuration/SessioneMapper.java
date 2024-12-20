package com.is.mindart.configuration;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.service.SessioneDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessioneMapper {
    private final ModelMapper modelMapper;
    private final BambinoRepository bambinoRepository;
    private final MaterialeRepository materialeRepository;

    /**
     * Inject del modelMapper e delle repository necessarie.
     * Chiama il metodo di configurazione del modelMapper
     * @param modelMapper - default model mapper
     * @param bambinoRepository - repository del bambino
     * @param materialeRepository - repository del materiale
     */
    @Autowired
    public SessioneMapper(final ModelMapper modelMapper,
                          final BambinoRepository bambinoRepository,
                          final MaterialeRepository materialeRepository) {
        this.modelMapper = modelMapper;
        this.bambinoRepository = bambinoRepository;
        this.materialeRepository = materialeRepository;
        configureMappings();
    }

    private void configureMappings() {
        // Configurazione personalizzata per il mapping da SessioneDTO a Sessione
        modelMapper.createTypeMap(SessioneDTO.class, Sessione.class)
                .addMappings(mapper -> {
                    // Mappa i campi diretti
                    mapper.map(SessioneDTO::getTipoSessione, Sessione::setTipo);
                    mapper.map(SessioneDTO::getTemaAssegnato, Sessione::setTemaAssegnato);

                    // Usa un converter per mappare il campo `bambini`
                    mapper.using(ctx -> mapBambini((List<Long>) ctx.getSource()))
                            .map(SessioneDTO::getBambini, Sessione::setBambini);

                    // Usa un converter per mappare il campo `materiale`
                    mapper.using(ctx -> mapMateriale((Long) ctx.getSource()))
                            .map(SessioneDTO::getMateriale, Sessione::setMateriale);
                });
    }

    private List<Bambino> mapBambini(List<Long> bambiniIds) {
        if (bambiniIds == null || bambiniIds.isEmpty()) {
            return Collections.emptyList();
        }
        return bambiniIds.stream()
                .map(id -> bambinoRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Bambino con ID " + id + " non trovato")))
                .collect(Collectors.toList());
    }

    private Materiale mapMateriale(Long materialeId) {
        if (materialeId == null) {
            return null;
        }
        return materialeRepository.findById(materialeId)
                .orElseThrow(() -> new IllegalArgumentException("Materiale con ID " + materialeId + " non trovato"));
    }

    public Sessione toEntity(SessioneDTO sessioneDTO) {
        Sessione sessione = modelMapper.map(sessioneDTO, Sessione.class);
        sessione.setData(LocalDateTime.now());
        sessione.setNota("");
        return sessione;
    }
}

