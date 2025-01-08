package com.is.mindart.configuration;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneMateriale.model.MaterialeRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.service.SessioneDTO;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SessioneMapper {

    /**
     * Istanza iniettata del modelMapper.
     */
    private final ModelMapper modelMapper;
    /**
     * Istanza iniettata del repository dei bambini.
     */
    private final BambinoRepository bambinoRepository;
    /**
     * Istanza iniettata del repository dei materiali.
     */
    private final MaterialeRepository materialeRepository;

    @PostConstruct
    private void configureMappings() {
        // =============== Mapping da SessioneDTO a Sessione ===============
        modelMapper.createTypeMap(SessioneDTO.class, Sessione.class)
                .addMappings(mapper -> {
                    // Mappa i campi diretti
                    mapper.map(SessioneDTO::getTipoSessione, Sessione::setTipo);
                    mapper.map(SessioneDTO::getTemaAssegnato,
                            Sessione::setTemaAssegnato);

                    // Usa un converter per mappare il campo `bambini`
                    mapper.using(ctx ->
                                    mapBambini((List<Long>) ctx.getSource()))
                            .map(SessioneDTO::getBambini, Sessione::setBambini);

                    // Usa un converter per mappare il campo `materiale`
                    mapper.using(ctx -> mapMateriale((Long) ctx.getSource()))
                            .map(SessioneDTO::getMateriale,
                                    Sessione::setMateriale);
                });

    }

    /**
     * Converte la lista di id di bambini in lista di entità {@link Bambino}.
     * @param bambiniIds lista di id di bambini
     * @return lista di entità {@link Bambino}
     */
    private List<Bambino> mapBambini(
            final List<Long> bambiniIds) {
        if (bambiniIds == null || bambiniIds.isEmpty()) {
            return Collections.emptyList();
        }
        return bambiniIds.stream()
                .map(id -> bambinoRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Bambino con ID " + id + " non trovato")))
                .collect(Collectors.toList());
    }

    /**
     * Converte un id di materiale nell'entità {@link Materiale}.
     * @param materialeId id del materiale
     * @return entità {@link Materiale}
     */
    private Materiale mapMateriale(
            final Long materialeId) {
        if (materialeId == null) {
            return null;
        }
        return materialeRepository.findById(materialeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Materiale con ID " + materialeId + " non trovato"));
    }

    /**
     * Converte una lista di entità {@link Bambino} in lista di id.
     * @param bambini lista di entità {@link Bambino}
     * @return lista di id
     */
    private List<Long> mapBambiniIds(
            final List<Bambino> bambini) {
        if (bambini == null || bambini.isEmpty()) {
            return Collections.emptyList();
        }
        return bambini.stream()
                .map(Bambino::getId)
                .collect(Collectors.toList());
    }

    /**
     * Metodo per convertire da DTO a Entity (Sessione).
     * @param sessioneDTO DTO da convertire
     * @return Entity (Sessione) convertita
     */
    public Sessione toEntity(
            final SessioneDTO sessioneDTO) {
        Sessione sessione = modelMapper.map(sessioneDTO, Sessione.class);
        sessione.setData(LocalDateTime.now());
        sessione.setNota("");
        sessione.setTerminata(false);
        return sessione;
    }

}
