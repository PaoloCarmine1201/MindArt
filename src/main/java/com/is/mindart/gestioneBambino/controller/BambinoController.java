
package com.is.mindart.gestioneBambino.controller;

import com.is.mindart.gestioneBambino.service.BambinoDTO;
import com.is.mindart.gestioneBambino.service.BambinoDTOSimple;
import com.is.mindart.gestioneBambino.service.BambinoService;
import com.is.mindart.gestioneBambino.service.RegisterBambinoDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/terapeuta/bambino")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class BambinoController {

    /**
     * Servizio per la gestione dei bambini.
     */
    private final BambinoService bambinoService;

    /**
     * @author gabrieleristallo
     * Restituisce tutti i bambini presenti nel database.
     *
     * @return lista di bambini presenti nel database
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/getall")
    public ResponseEntity<List<BambinoDTO>> getAllBambini() {
        List<BambinoDTO> bambini = bambinoService.getAllBambini();
        return ResponseEntity.ok(bambini);
    }
    /**
     * @author gabrieleristallo
     * Restituisce tutti i bambini relativi a un terapeuta.
     * @return lista di bambini del terapeuta
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/getallbyterapeuta")
    public ResponseEntity<List<BambinoDTOSimple>> getAllBambiniByTerapeuta() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();


        List<BambinoDTOSimple> bambini = bambinoService
                .getBambiniByT((String) authentication.getPrincipal());
        return ResponseEntity.ok(bambini);
    }

    /**
     * @author gabrieleristallo
     * Restituisce il bambino con l'identificativo specificato.
     *
     * @param id identificativo del bambino
     * @return bambino con l'identificativo specificato
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/get/{id}")
    public ResponseEntity<BambinoDTOSimple> getBambino(
            @PathVariable final Long id
    ) {
        BambinoDTOSimple bambino = bambinoService.getBambino(id);
        return ResponseEntity.ok(bambino);
    }

    /**
     * @author gabrieleristallo
     * Aggiunge le informazioni del bambino al database.
     *
     * @param bambinoDto un oggetto {@link RegisterBambinoDTO}
     *                   contenente i dati del bambino.
     * @return bambino salvato come {@link RegisterBambinoDTO}
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping("/add")
    public ResponseEntity<RegisterBambinoDTO> addBambino(
            @Valid @RequestBody final RegisterBambinoDTO bambinoDto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        bambinoService.addBambino(bambinoDto, (String) authentication.getPrincipal());
        return ResponseEntity.ok(bambinoDto);
    }
    /**
     * @author mauriliolarocca
     * Aggiorna le informazioni del bambino al database.
     *
     * @param bambinoDto un oggetto {@link RegisterBambinoDTO}
     *                   contenente i dati del bambino.
     * @return bambino salvato come {@link RegisterBambinoDTO}
     */
    @PostMapping("/update")
    public ResponseEntity<RegisterBambinoDTO> updateBambino(
            @Valid @RequestBody final RegisterBambinoDTO bambinoDto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        bambinoService.updateBambino(bambinoDto, (String) authentication.getPrincipal());
        return ResponseEntity.ok(bambinoDto);
    }

    /**
     * Elimina un bambino dal database.
     *
     * @param id Identificativo del bambino.
     * @return Stringa di risposta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBambino(
            @PathVariable final Long id
    ) {
        bambinoService.deleteBambino(id);
        return ResponseEntity.ok("Il bambino con id ("
                + id
                + ") è stato eliminato con successo");
    }
}
