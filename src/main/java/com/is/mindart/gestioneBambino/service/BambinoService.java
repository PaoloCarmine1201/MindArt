package com.is.mindart.gestioneBambino.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneDisegno.model.DisegnoRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BambinoService {

    /**
     *  Provvede ad accedere al database per l'entità Bambino.
     */
    private final BambinoRepository bambinoRepository;

    /**
     *  Provvede ad accedere al database per l'entità Terapeuta.
     */
    private final TerapeutaRepository terapeutaRepository;

    /**
     *  Provvede a mappare l'entità Bambino con BambinoDTO e RegisterBambinoDTO.
     */
    private final ModelMapper modelMapper;

    /**
     *  Provvede a generare il token JWT.
     */
    private final JwtUtil jwtUtil;

    /**
     *  Provvede ad accedere al database per l'entità Sessione.
     */
    private final SessioneRepository sessioneRepository;
    private final DisegnoRepository disegnoRepository;


    /**
     * Questo metodo gestisce la richiesta di login per un bambino.
     * @param codice Il codice del bambino
     * @return Il token JWT
     */
    public String loginBambino(final String codice) {
        Optional<Bambino> bambino = bambinoRepository.findByCodice(codice);
        if (bambino.isPresent()) {
            // Verifica se esiste almeno una sessione programmata per oggi
            Sessione session = sessioneRepository.findByTerminataFalseAndBambini_CodiceOrderByDataAsc(codice).stream()
                    .findFirst()
                    .orElse(null);

            if (session == null) {
                return null;
            }

            // Verifica se la sessione non è già iniziata
            if (session.getData().isAfter(LocalDateTime.now())) {
                return null;
            }

            return jwtUtil.generateToken(bambino.get().getCodice(), "BAMBINO");
        }
        return null;
    }


    /**
     * @author gabrieleristallo
     * Restituisce tutti i bambini presenti nel database.
     *
     * @return lista di bambini presenti nel database.
     */
    public List<BambinoDTO> getAllBambini() {
        List<Bambino> bambini = bambinoRepository.findAll();
        return bambini.stream()
                .map(bambino -> modelMapper.map(bambino, BambinoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * @author gabrieleristallo
     * Restituisce il bambino con l'identificativo specificato.
     *
     * @param id identificativo del bambino.
     * @return bambino con l'identificativo specificato.
     */
    public BambinoDTOSimple getBambino(final Long id) {
        Bambino bambino = bambinoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Bambino non trovato"));
        return modelMapper.map(bambino, BambinoDTOSimple.class);
    }

    /**
     * @author gabrieleristallo
     * Restituisce tutti i bambini associati ad un terapeuta.
     *
     * @param email l'email del terapeuta.
     * @return List<BambinoDTO> lista di bambini del terapeuta.
     */
    public List<BambinoDTOSimple> getBambiniByT(final String email) {
        return bambinoRepository
                .findAllByTerapeutaId(
                        terapeutaRepository
                                .findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Terapeuta non trovato"
                                        )
                                )
                                .getId()
                )
                .stream()
                .map(this::mapToBambinoDto)
                .collect(Collectors.toList());
    }

    /**
     * @author mauriliolarocca
     * Aggiunge le informazioni del bambino al database.
     *
     * @param bambinoDto un oggetto {@link RegisterBambinoDTO}
     *                   contenente i dati del bambino.
     */
    public void addBambino(final RegisterBambinoDTO bambinoDto) {

        Bambino bambino = modelMapper.map(bambinoDto, Bambino.class);
        bambino.setTerapeuta(
                terapeutaRepository
                        .findById(bambinoDto.getTerapeutaId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Terapeuta non trovato"
                                )
                        )
        );
        bambinoRepository.save(bambino);
    }

    private BambinoDTOSimple mapToBambinoDto(Bambino bambino) {
        return modelMapper.map(bambino, BambinoDTOSimple.class);
    }
}
