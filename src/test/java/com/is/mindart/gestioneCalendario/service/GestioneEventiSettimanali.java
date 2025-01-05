package com.is.mindart.gestioneCalendario.service;

import com.is.mindart.gestioneCalendario.exception.EventNotFoundException;
import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneCalendario.model.EventoRespository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class GestioneEventiSettimanali {

    @Test
    @DisplayName("Entrambe le date sono null -> FALSE")
    void shouldReturnFalseWhenBothDatesAreNull() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(null);
        eventDto.setFine(null);
        assertFalse(eventDto.isDateRangeValid());
    }

    @Test
    @DisplayName("Data di inizio NULL -> FALSE")
    void shouldReturnFalseWhenStartDateIsNull() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(null);
        eventDto.setFine(new Date());
        assertFalse(eventDto.isDateRangeValid());
    }

    @Test
    @DisplayName("Data di fine NULL -> FALSE")
    void shouldReturnFalseWhenEndDateIsNull() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(new Date());
        eventDto.setFine(null);
        assertFalse(eventDto.isDateRangeValid());
    }

    @Test
    @DisplayName("Data di inizio e data di fine uguali -> FALSE")
    void shouldReturnFalseWhenStartDateEqualsEndDate() {
        Date now = new Date();
        EventDto eventDto = new EventDto();
        eventDto.setInizio(now);
        eventDto.setFine(now);
        assertFalse(eventDto.isDateRangeValid());
    }

    @Test
    @DisplayName("Data di fine precedente alla data di inizio -> FALSE")
    void shouldReturnFalseWhenEndDateIsBeforeStartDate() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(new Date(System.currentTimeMillis() + 1000));
        eventDto.setFine(new Date());
        assertFalse(eventDto.isDateRangeValid());
    }

    @Test
    @DisplayName("Data di inizio prima della data di fine -> TRUE")
    void shouldReturnTrueWhenStartDateIsBeforeEndDate() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(new Date());
        eventDto.setFine(new Date(System.currentTimeMillis() + 1000));
        assertTrue(eventDto.isDateRangeValid());
    }

    EventService eventService = new EventService(mock(EventoRespository.class), mock(TerapeutaRepository.class), mock(ModelMapper.class));

    @Test
    @DisplayName("Ottenere tutti gli eventi -> Lista non vuota")
    void getAllEvents() {
        List<EventDto> events = eventService.getAllEvents("email");
        assertNotNull(events);
    }

    @Test
    @DisplayName("Modifica di un evento inesistente -> EventNotFoundException")
    void updateEventIfNotExists() {
        EventDto eventDto = new EventDto();
        eventDto.setId(1999L);
        eventDto.setNome("Evento");
        eventDto.setInizio(null);
        eventDto.setFine(null);

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent(eventDto, "email"));
    }

    @Test
    @DisplayName("Eliminazione di un evento inesistente -> EventNotFoundException")
    void deleteEventIfNotExists() {
        assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent(1999L, "email"));
    }

    @Test
    @DisplayName("Recupero di un evento inesistente -> EventNotFoundException")
    void getEventByIdAndTerapeutaEmailIfNotExists() {
        assertThrows(EventNotFoundException.class, () -> eventService.getEventByIdAndTerapeutaEmail(1999L, "email"));
    }

    @Test
    @DisplayName("Aggiunta di un evento -> EventDto")
    void addEvent() {
        Terapeuta terapeuta = new Terapeuta(1L, "nome", "cognome", "email", new Date(),
                "password", null, null, null, null, null);
        Evento evento = new Evento(1L, "Evento", new Date(),
                new Date(System.currentTimeMillis() + 1000), terapeuta);
        EventDto eventDto = new EventDto(1L, "Evento", new Date(),
                new Date(System.currentTimeMillis() + 1000));

        TerapeutaRepository terapeutaRepository = mock(TerapeutaRepository.class);
        EventoRespository eventoRespository = mock(EventoRespository.class);
        ModelMapper modelMapper = mock(ModelMapper.class);

        when(terapeutaRepository.findByEmail("email")).thenReturn(Optional.of(terapeuta));
        when(eventoRespository.save(any(Evento.class))).thenReturn(evento);
        when(modelMapper.map(eventDto, Evento.class)).thenReturn(evento);
        when(modelMapper.map(evento, EventDto.class)).thenReturn(eventDto);

        EventService eventService = new EventService(eventoRespository, terapeutaRepository, modelMapper);
        EventDto result = eventService.addEvent(eventDto, "email");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Aggiornamento di un evento -> EventDto")
    void updateEvent() {
        Terapeuta terapeuta = new Terapeuta(1L, "nome", "cognome", "email", new Date(),
                "password", null, null, null, null, null);
        Evento evento = new Evento(1L, "Evento", new Date(),
                new Date(System.currentTimeMillis() + 1000), terapeuta);
        EventDto eventDto = new EventDto(1L, "Evento", new Date(),
                new Date(System.currentTimeMillis() + 1000));

        TerapeutaRepository terapeutaRepository = mock(TerapeutaRepository.class);
        EventoRespository eventoRespository = mock(EventoRespository.class);
        ModelMapper modelMapper = mock(ModelMapper.class);

        when(eventoRespository.findById(1L)).thenReturn(Optional.of(evento));
        when(eventoRespository.save(any(Evento.class))).thenReturn(evento);
        when(modelMapper.map(eventDto, Evento.class)).thenReturn(evento);
        when(modelMapper.map(evento, EventDto.class)).thenReturn(eventDto);

        EventService eventService = new EventService(eventoRespository, terapeutaRepository, modelMapper);
        EventDto result = eventService.updateEvent(eventDto, "email");

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Eliminazione di un evento -> EventDto")
    void deleteEvent() {
        Terapeuta terapeuta = new Terapeuta(1L, "nome", "cognome", "email", new Date(),
                "password", null, null, null, null, null);
        Evento evento = new Evento(1L, "Evento", new Date(),
                new Date(System.currentTimeMillis() + 1000), terapeuta);

        TerapeutaRepository terapeutaRepository = mock(TerapeutaRepository.class);
        EventoRespository eventoRespository = mock(EventoRespository.class);
        ModelMapper modelMapper = mock(ModelMapper.class);

        when(eventoRespository.findByIdAndTerapeutaEmail(1L, "email"))
                .thenReturn(Optional.of(evento));

        EventService eventService = new EventService(eventoRespository, terapeutaRepository, modelMapper);

        eventService.deleteEvent(1L, "email");

        verify(eventoRespository, times(1)).delete(evento);
        assertNull(eventService.getEventByIdAndTerapeutaEmail(1L, "email"));
    }

}
