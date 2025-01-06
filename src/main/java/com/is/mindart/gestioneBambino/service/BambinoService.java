package com.is.mindart.gestioneBambino.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneSessione.model.Sessione;
import com.is.mindart.gestioneSessione.model.SessioneRepository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import com.is.mindart.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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

    /**
     * Questo metodo gestisce la richiesta di login per un bambino.
     * @param codice Il codice del bambino
     * @return Il token JWT
     */
    public String loginBambino(final String codice) {
        Optional<Bambino> bambino = bambinoRepository.findByCodice(codice);
        if (bambino.isPresent()) {
            // Verifica se esiste almeno una sessione programmata per oggi
            Sessione session = sessioneRepository
                    .findByTerminataFalseAndBambini_CodiceOrderByDataAsc(codice)
                    .stream()
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
     * Restituisce tutti i bambini associati a un terapeuta.
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
                .filter(bambino -> bambino.getVisibile() == Boolean.TRUE)
                .map(this::mapToBambinoDto)
                .collect(Collectors.toList());
    }

    /**
     * @author mauriliolarocca
     * Aggiunge le informazioni del bambino al database.
     *
     * @param bambinoDto un oggetto {@link RegisterBambinoDTO}
     *                   contenente i dati del bambino.
     * @param terapeutaEmail l'email del terapeuta.
     */
    public void addBambino(
            final RegisterBambinoDTO bambinoDto,
            final String terapeutaEmail
    ) {
        // Recupera il terapeuta dal database
        Terapeuta terapeuta = terapeutaRepository.findByEmail(terapeutaEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Terapeuta non trovato")
                );
        // Mappa il DTO in un oggetto Bambino
        Bambino bambino = modelMapper.map(bambinoDto, Bambino.class);
        // Imposta il terapeuta
        bambino.setTerapeuta(terapeuta);
        bambino.setVisibile(true);
        // Genera un codice univoco per il bambino
        do {
            bambino.setCodice(generateRandomCode());
        } while (
                bambinoRepository
                        .findByCodice(bambino.getCodice())
                        .isPresent()
        );
        bambinoRepository.save(bambino);
    }

    /**
     * @author mauriliolarocca
     * Aggiorna le informazioni del bambino al database.
     *
     * @param bambinoDto un oggetto {@link RegisterBambinoDTO}
     *                   contenente i dati del bambino.
     * @param terapeutaEmail l'email del terapeuta.
     */
    public void updateBambino(
            final RegisterBambinoDTO bambinoDto,
            final String terapeutaEmail
    ) {
        // Recupera il terapeuta dal database
        Terapeuta terapeuta = terapeutaRepository.findByEmail(terapeutaEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Terapeuta non trovato"));
        // Recupera l'identificativo del bambino
        Long bambinoId = bambinoDto.getId();

        // Verifica che il bambino sia associato al terapeuta
        if (
                terapeuta
                        .getBambini()
                        .stream()
                        .anyMatch(bambino -> bambino.getId().equals(bambinoId))
        ) {
            // Aggiorna le informazioni del bambino
            // Non ho usato il model mapper per evitare di sovrascrivere
            // i campi che non devono essere modificati
            Bambino bambino = bambinoRepository.getReferenceById(bambinoId);
            bambino.setNome(bambinoDto.getNome());
            bambino.setCognome(bambinoDto.getCognome());
            bambino.setDataDiNascita(bambinoDto.getDataDiNascita());
            bambino.setSesso(bambinoDto.getSesso());
            bambino.setCodiceFiscale(bambinoDto.getCodiceFiscale());
            bambino.setEmailGenitore(bambinoDto.getEmailGenitore());
            bambino.setTelefonoGenitore(bambinoDto.getTelefonoGenitore());
            bambinoRepository.save(bambino);
        } else {
            throw new IllegalArgumentException("Bambino non trovato");
        }
    }



    /**
     * Questo metodo gestisce la richiesta di delete per un bambino.
     * @param id Il codice del bambino
     */
    public void deleteBambino(final Long id) {
        Bambino bambino = bambinoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Bambino non trovato"));
        bambino.setVisibile(false);
        bambinoRepository.save(bambino);
    }

    private BambinoDTOSimple mapToBambinoDto(final Bambino bambino) {
        return modelMapper.map(bambino, BambinoDTOSimple.class);
    }
    // Genera un codice univoco per il bambino
    private String generateRandomCode() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder newCodice = new StringBuilder();
        // Genera 3 lettere casuali
        for (int i = 0; i < 3; i++) {
            newCodice.append(letters.charAt(random.nextInt(letters.length())));
        }

        // Genera 3 numeri casuali
        for (int i = 0; i < 3; i++) {
            newCodice.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        return newCodice.toString();
    }
}
