package com.is.mindart.gestioneBambino.service;

import com.is.mindart.gestioneBambino.model.Bambino;
import com.is.mindart.gestioneBambino.model.BambinoRepository;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BambinoService {

    /**
     *  Provvede ad accedere al database per l'entità Bambino.
     */
    @Autowired
    private BambinoRepository repository;

    /**
     *  Provvede ad accedere al database per l'entità Terapeuta.
     */
    @Autowired
    private TerapeutaRepository terapeutaRepository;

    /**
     *  Provvede a mappare l'entità Bambino con BambinoDTO e RegisterBambinoDTO.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * @author gabrieleristallo
     * Restituisce tutti i bambini presenti nel database.
     *
     * @return lista di bambini presenti nel database.
     */
    public List<BambinoDTO> getAllBambini() {
        List<Bambino> bambini = repository.findAll();
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
    public BambinoDTO getBambino(final Long id) {
        Bambino bambino = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Bambino non trovato"));
        return modelMapper.map(bambino, BambinoDTO.class);
    }

    //TODO da scrivere meglio e da aggiungere javadoc corretto.
    /**
     * @author gabrieleristallo
     * Restituisce bambini di un terapeuta.
     *
     * @param terapeuta identificativo del terapeuta.
     * @return lista di bambini del terapeuta.
     */
    public List<BambinoDTO> getBambiniByT(final Long terapeuta) {
        List<Bambino> bambini = repository.findAll();

        return bambini.stream()
                .filter(bambino ->
                        bambino.getTerapeuta().getId().equals(terapeuta))
                .map(bambino -> modelMapper.map(bambino, BambinoDTO.class))
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
        repository.save(bambino);
    }
}
