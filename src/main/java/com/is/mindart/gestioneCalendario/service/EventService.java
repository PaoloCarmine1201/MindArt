package com.is.mindart.gestioneCalendario.service;

import com.is.mindart.gestioneCalendario.exception.EventNotFoundException;
import com.is.mindart.gestioneCalendario.model.EventRespository;
import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service per la gestione degli eventi del calendario.
 * Fornisce metodi per recuperare, aggiungere, aggiornare ed eliminare eventi.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    /**
     * Repository per la gestione delle operazioni sugli eventi.
     */
    private final EventRespository eventRepository;

    /**
     * Repository per la gestione delle operazioni sui terapeuti.
     */
    private final TerapeutaRepository terapeutaRepository;

    /**
     * Mapper per la conversione tra {@link Evento} e {@link EventDto}.
     */
    private final ModelMapper modelMapper;

    /**
     * Recupera tutti gli eventi associati a un terapeuta specifico.
     *
     * @param id l'identificativo del terapeuta
     * @return una lista di {@link EventDto} associati al terapeuta
     */
    public List<EventDto> getAllEvents(Long id) {
        return eventRepository.findAllByTerapeutaId(id).stream()
                .map(this::mapToEventDto)
                .collect(Collectors.toList());
    }

    /**
     * Recupera un evento specifico tramite il suo ID.
     *
     * @param id l'identificativo dell'evento
     * @return un oggetto {@link EventDto} rappresentante l'evento
     * @throws EventNotFoundException se l'evento non esiste
     */
    public EventDto getEventById(Long id) {
        Evento event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return mapToEventDto(event);
    }

    /**
     * Aggiunge un nuovo evento al calendario.
     *
     * @param eventDto un oggetto {@link EventDto} rappresentante i dati dell'evento
     * @return l'evento salvato come {@link EventDto}
     */
    public EventDto addEvent(EventDto eventDto) {
        Evento event = mapToEvent(eventDto);
        Evento savedEvent = eventRepository.save(event);
        return mapToEventDto(savedEvent);
    }

    /**
     * Aggiorna un evento esistente nel calendario.
     *
     * @param eventDto un oggetto {@link EventDto} con i dati aggiornati dell'evento
     * @return l'evento aggiornato come {@link EventDto}
     * @throws EventNotFoundException se l'evento non esiste
     */
    public EventDto updateEvent(EventDto eventDto) {
        Evento existingEvent = eventRepository.findById(eventDto.getId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        // Aggiorna i campi necessari
        existingEvent.setNome(eventDto.getNome());
        existingEvent.setInizio(eventDto.getInizio());
        existingEvent.setFine(eventDto.getFine());

        Evento updatedEvent = eventRepository.save(existingEvent);
        return mapToEventDto(updatedEvent);
    }

    /**
     * Elimina un evento dal calendario.
     *
     * @param id l'identificativo dell'evento da eliminare
     * @throws EventNotFoundException se l'evento non esiste
     */
    public void deleteEvent(Long id) {
        Evento event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        eventRepository.delete(event);
    }

    /**
     * Converte un'entità {@link Evento} in un DTO {@link EventDto}.
     *
     * @param event l'entità da convertire
     * @return il DTO corrispondente
     */
    private EventDto mapToEventDto(Evento event) {
        return modelMapper.map(event, EventDto.class);
    }

    /**
     * Converte un DTO {@link EventDto} in un'entità {@link Evento}.
     *
     * @param eventDto il DTO da convertire
     * @return l'entità corrispondente
     */
    private Evento mapToEvent(EventDto eventDto) {
        Evento evento = modelMapper.map(eventDto, Evento.class);
        evento.setTerapeuta(terapeutaRepository.getReferenceById(eventDto.getTerapeuta()));
        return evento;
    }
}
