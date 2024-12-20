package com.is.mindart.gestioneCalendario.controller;

import com.is.mindart.gestioneCalendario.service.EventDto;
import com.is.mindart.gestioneCalendario.service.EventService;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.security.model.TerapeutaDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

/**
 * Controller per la gestione degli eventi del calendario.
 * Fornisce endpoint per operazioni CRUD sugli eventi.
 */
@RestController
@RequestMapping("/api/terapeuta")
@RequiredArgsConstructor
public class EventChildController {
    /**
     * Servizio per la gestione della logica degli eventi del calendario.
     */
    private final EventService eventService;

    /**
     * Recupera tutti gli eventi associati a un terapeuta specifico.
     *
     * @return una lista di {@link EventDto} associati al terapeuta
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        // Ora hai accesso al terapeuta completo
        Terapeuta terapeuta = principal.getTerapeuta();
        Long terapeutaId = terapeuta.getId();
        List<EventDto> events = eventService.getAllEvents(terapeutaId);
        return ResponseEntity.ok(events);
    }

    /**
     * Recupera un evento specifico tramite il suo ID.
     *
     * @param id l'identificativo dell'evento
     * @return un {@link EventDto} rappresentante l'evento
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @GetMapping("/event/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable final Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        // Ora hai accesso al terapeuta completo
        Terapeuta terapeuta = principal.getTerapeuta();
        Long terapeutaId = terapeuta.getId();

        EventDto event = eventService.getEventByIdAndTerapeutaId(id, terapeutaId);
        return ResponseEntity.ok(event);
    }

    /**
     * Aggiorna un evento esistente nel calendario.
     *
     * @param eventDto un oggetto {@link EventDto}
     *        contenente i dati aggiornati dell'evento
     * @return l'evento aggiornato come {@link EventDto}
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PutMapping("/event")
    public ResponseEntity<EventDto> updateEvent(
            @Valid @RequestBody EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        EventDto updatedEvent = eventService.updateEvent(eventDto, principal.getTerapeuta().getId());
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Elimina un evento dal calendario.
     *
     * @param id l'identificativo dell'evento da eliminare
     * @return una risposta HTTP senza corpo
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @DeleteMapping("/event/{id}")
    public ResponseEntity<Void> deleteEvent(
            @NotNull(message = "Il campo id deve essere valorizzato")
            @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        eventService.deleteEvent(id, principal.getTerapeuta().getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Aggiunge un nuovo evento al calendario.
     *
     * @param eventDto un oggetto {@link EventDto} contenente i dati dell'evento
     * @return l'evento salvato come {@link EventDto}
     */
    @PreAuthorize("hasRole('TERAPEUTA')")
    @PostMapping("/event")
    public ResponseEntity<EventDto> addEvent(
            @RequestBody EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TerapeutaDetails principal = (TerapeutaDetails) authentication.getPrincipal();

        EventDto event = eventService.addEvent(eventDto, principal.getTerapeuta().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
}
