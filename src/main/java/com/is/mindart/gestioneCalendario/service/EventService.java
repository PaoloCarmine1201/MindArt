package com.is.mindart.gestioneCalendario.service;

import com.is.mindart.gestioneCalendario.exception.EventNotFoundException;
import com.is.mindart.gestioneCalendario.model.EventoRespository;
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
    private final EventoRespository eventRepository;

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
     * @param email l'email del terapeuta
     * @return una lista di {@link EventDto} associati al terapeuta
     */
    public List<EventDto> getAllEvents(final String email) {
        return eventRepository.findByTerapeutaEmail(email).stream()
                .map(this::mapToEventDto)
                .collect(Collectors.toList());
    }

    /**
     * Recupera un evento specifico tramite il suo ID.
     * @param idEvento l'identificativo dell'evento
     * @param email l'email del terapeuta
     * @return un oggetto {@link EventDto} rappresentante l'evento
     */
    public EventDto getEventByIdAndTerapeutaEmail(
            final Long idEvento, final String email) {
        Evento event = eventRepository
                .findByIdAndTerapeutaEmail(idEvento, email)
                .orElseThrow(() ->
                        new EventNotFoundException("Event not found"));
        return mapToEventDto(event);
    }

    /**
     * Aggiunge un nuovo evento al calendario.
     *
     * @param eventDto un oggetto {@link EventDto}
     *                 rappresentante i dati dell'evento
     * @param email l'email del terapeuta
     * @return l'evento salvato come {@link EventDto}
     */
    public EventDto addEvent(
            final EventDto eventDto, final String email) {
        Evento event = mapToEvent(eventDto);
        event.setTerapeuta(terapeutaRepository.findByEmail(email)
                .orElseThrow(()
                        -> new EventNotFoundException("Terapeuta not found")));
        Evento savedEvent = eventRepository.save(event);
        return mapToEventDto(savedEvent);
    }
    /**
     * Aggiorna un evento esistente nel calendario.
     *
     * @param eventDto un oggetto {@link EventDto}
     *                 con i dati aggiornati dell'evento
     * @param email l'email del terapeuta
     * @return l'evento aggiornato come {@link EventDto}
     * @throws EventNotFoundException se l'evento non esiste
     */
    public EventDto updateEvent(final EventDto eventDto, final String email) {
        Evento existingEvent = eventRepository.findById(eventDto.getId())
                .orElseThrow(() ->
                        new EventNotFoundException("Event not found"));

        // Aggiorna i campi necessari
        existingEvent.setNome(eventDto.getNome());
        existingEvent.setInizio(eventDto.getInizio());
        existingEvent.setFine(eventDto.getFine());

        if (!existingEvent.getTerapeuta().getEmail().equals(email)) {
            throw new EventNotFoundException("Event not found");
        }
        Evento updatedEvent = eventRepository.save(existingEvent);
        return mapToEventDto(updatedEvent);
    }

    /**
     * Elimina un evento dal calendario.
     *
     * @param id l'identificativo dell'evento da eliminare
     * @param email l'email del terapeuta
     * @throws EventNotFoundException se l'evento non esiste
     */
    public void deleteEvent(final Long id, final String email) {
        Evento event = eventRepository.findByIdAndTerapeutaEmail(id, email)
                .orElseThrow(() ->
                        new EventNotFoundException("Event not found"));
        eventRepository.delete(event);
    }

    /**
     * Converte un'entità {@link Evento} in un DTO {@link EventDto}.
     *
     * @param event l'entità da convertire
     * @return il DTO corrispondente
     */
    private EventDto mapToEventDto(final Evento event) {
        return modelMapper.map(event, EventDto.class);
    }

    /**
     * Converte un DTO {@link EventDto} in un'entità {@link Evento}.
     *
     * @param eventDto il DTO da convertire
     * @return l'entità corrispondente
     */
    private Evento mapToEvent(final EventDto eventDto) {
        return modelMapper.map(eventDto, Evento.class);
    }
}
