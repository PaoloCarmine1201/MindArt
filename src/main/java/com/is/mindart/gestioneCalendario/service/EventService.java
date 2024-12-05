package com.is.mindart.gestioneCalendario.service;

import com.is.mindart.gestioneCalendario.exception.EventNotFoundException;
import com.is.mindart.gestioneCalendario.model.EventRespository;
import com.is.mindart.gestioneCalendario.model.Evento;
import java.util.List;
import java.util.stream.Collectors;

import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRespository eventRepository;
    private final TerapeutaRepository terapeutaRepository;
    private final ModelMapper modelMapper;

    public List<EventDto> getAllEvents(Long id) {
        return eventRepository.findAllByTerapeutaId(id).stream()
                .map(this::mapToEventDto)
                .collect(Collectors.toList());
    }

    public EventDto getEventById(Long id) {
        Evento event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        return mapToEventDto(event);
    }

    public EventDto addEvent(EventDto eventDto) {
        Evento event = mapToEvent(eventDto);
        Evento savedEvent = eventRepository.save(event);
        return mapToEventDto(savedEvent);
    }

    public EventDto updateEvent(EventDto eventDto) {
        Evento existingEvent = eventRepository.findById(eventDto.getId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        // Aggiorna i campi necessari
        existingEvent.setNome(eventDto.getNome());
        existingEvent.setInizio(eventDto.getInizio());
        existingEvent.setFine(eventDto.getFine());
        existingEvent.setTerapeuta(terapeutaRepository.getById(eventDto.getId())); // Assicurati che Terapeuta sia mappato correttamente

        Evento updatedEvent = eventRepository.save(existingEvent);
        return mapToEventDto(updatedEvent);
    }

    public void deleteEvent(Long id) {
        Evento event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        eventRepository.delete(event);
    }

    private EventDto mapToEventDto(Evento event) {
        return modelMapper.map(event, EventDto.class);
    }

    private Evento mapToEvent(EventDto eventDto) {
        return modelMapper.map(eventDto, Evento.class);
    }
}

