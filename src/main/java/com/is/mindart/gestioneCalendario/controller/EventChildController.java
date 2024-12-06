package com.is.mindart.gestioneCalendario.controller;

import com.is.mindart.gestioneCalendario.service.EventDto;
import com.is.mindart.gestioneCalendario.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;

/**
 * Controller per la gestione degli eventi del calendario.
 * Fornisce endpoint per operazioni CRUD sugli eventi.
 */
@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventChildController {
    /**
     * Servizio per la gestione della logica degli eventi del calendario.
     */
    private final EventService eventService;

    /**
     * Recupera tutti gli eventi associati a un terapeuta specifico.
     *
     * @param id l'identificativo del terapeuta
     * @return una lista di {@link EventDto} associati al terapeuta
     */
    @GetMapping("/terapeuta/{id}/all")
    public ResponseEntity<List<EventDto>> getAllEvents(@PathVariable Long id) {
        List<EventDto> events = eventService.getAllEvents(id);
        return ResponseEntity.ok(events);
    }

    /**
     * Recupera un evento specifico tramite il suo ID.
     *
     * @param id l'identificativo dell'evento
     * @return un {@link EventDto} rappresentante l'evento
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable final Long id) {
        EventDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    /**
     * Aggiunge un nuovo evento al calendario.
     *
     * @param eventDto un oggetto {@link EventDto} contenente i dati dell'evento
     * @return l'evento salvato come {@link EventDto}
     */
    @PostMapping("/add")
    public ResponseEntity<EventDto> addEvent(
            @RequestBody EventDto eventDto) {
        EventDto event = eventService.addEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    /**
     * Aggiorna un evento esistente nel calendario.
     *
     * @param eventDto un oggetto {@link EventDto}
     *        contenente i dati aggiornati dell'evento
     * @return l'evento aggiornato come {@link EventDto}
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<EventDto> updateEvent(
            @RequestBody EventDto eventDto) {
        EventDto updatedEvent = eventService.updateEvent(eventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Elimina un evento dal calendario.
     *
     * @param id l'identificativo dell'evento da eliminare
     * @return una risposta HTTP senza corpo
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
