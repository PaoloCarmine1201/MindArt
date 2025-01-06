package com.is.mindart.gestioneCalendario.service;

import com.is.mindart.gestioneCalendario.exception.EventNotFoundException;
import com.is.mindart.gestioneCalendario.model.Evento;
import com.is.mindart.gestioneCalendario.model.EventoRespository;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import com.is.mindart.gestioneTerapeuta.model.TerapeutaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Classe di test per la gestione degli eventi settimanali.
 */
@ExtendWith(MockitoExtension.class)
class GestioneEventiSettimanali {

    /**
     * Repository per la gestione degli eventi.
     */
    @Mock
    private EventoRespository eventoRespository;

    /**
     * Repository per la gestione dei terapeuti.
     */
    @Mock
    private TerapeutaRepository terapeutaRepository;

    /**
     * Mapper per la conversione tra DTO e entità.
     */
    @Mock
    private ModelMapper modelMapper;

    /**
     * Servizio per la gestione degli eventi, iniettato con i mock.
     */
    @InjectMocks
    private EventService eventService;

    /**
     * Test per verificare il comportamento quando entrambe le date sono null.
     * Deve ritornare FALSE.
     */
    @Test
    @DisplayName("Entrambe le date sono null -> FALSE")
    void shouldReturnFalseWhenBothDatesAreNullTest() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(null);
        eventDto.setFine(null);
        assertFalse(eventDto.isDateRangeValid());
    }

    /**
     * Test per verificare il comportamento quando la data di inizio è null.
     * Deve ritornare FALSE.
     */
    @Test
    @DisplayName("Data di inizio NULL -> FALSE")
    void shouldReturnFalseWhenStartDateIsNullTest() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(null);
        eventDto.setFine(new Date());
        assertFalse(eventDto.isDateRangeValid());
    }

    /**
     * Test per verificare il comportamento quando la data di fine è null.
     * Deve ritornare FALSE.
     */
    @Test
    @DisplayName("Data di fine NULL -> FALSE")
    void shouldReturnFalseWhenEndDateIsNullTest() {
        EventDto eventDto = new EventDto();
        eventDto.setInizio(new Date());
        eventDto.setFine(null);
        assertFalse(eventDto.isDateRangeValid());
    }

    /**
     * Test per verificare il comportamento quando
     * la data di inizio e fine sono uguali.
     * Deve ritornare FALSE.
     */
    @Test
    @DisplayName("Data di inizio e data di fine uguali -> FALSE")
    void shouldReturnFalseWhenStartDateEqualsEndDateTest() {
        Date now = new Date();
        EventDto eventDto = new EventDto();
        eventDto.setInizio(now);
        eventDto.setFine(now);
        assertFalse(eventDto.isDateRangeValid());
    }

    /**
     * Test per verificare il comportamento quando
     * la data di fine è precedente alla data di inizio.
     * Deve ritornare FALSE.
     */
    @Test
    @DisplayName("Data di fine precedente alla data di inizio -> FALSE")
    void shouldReturnFalseWhenEndDateIsBeforeStartDateTest() {
        final int timeOffset = 1000;

        EventDto eventDto = new EventDto();
        eventDto.setInizio(new Date(System.currentTimeMillis() + timeOffset));
        eventDto.setFine(new Date());
        assertFalse(eventDto.isDateRangeValid());
    }

    /**
     * Test per verificare il comportamento quando
     * la data di inizio è prima della data di fine.
     * Deve ritornare TRUE.
     */
    @Test
    @DisplayName("Data di inizio prima della data di fine -> TRUE")
    void shouldReturnTrueWhenStartDateIsBeforeEndDateTest() {
        final int timeOffset = 1000;

        EventDto eventDto = new EventDto();
        eventDto.setInizio(new Date());
        eventDto.setFine(new Date(System.currentTimeMillis() + timeOffset));
        assertTrue(eventDto.isDateRangeValid());
    }

    /**
     * Test per ottenere tutti gli eventi. Deve ritornare una lista non nulla.
     */
    @Test
    @DisplayName("Ottenere tutti gli eventi -> Lista non vuota")
    void getAllEventsTest() {
        final String email = "email";

        when(eventService.getAllEvents(email)).thenReturn(List.of());

        List<EventDto> events = eventService.getAllEvents(email);
        assertNotNull(events);

        verify(eventService, times(1)).getAllEvents(email);
    }

    /**
     * Test per la modifica di un evento inesistente.
     * Deve lanciare un'eccezione EventNotFoundException.
     */
    @Test
    @DisplayName("Modifica di un evento inesistente -> EventNotFoundException")
    void updateEventIfNotExistsTest() {
        final long nonExistentEventId = 1999L;
        final String email = "email";

        EventDto eventDto = new EventDto();
        eventDto.setId(nonExistentEventId);
        eventDto.setNome("Evento");
        eventDto.setInizio(null);
        eventDto.setFine(null);

        doThrow(new EventNotFoundException(
                "Evento non trovato con id " + nonExistentEventId))
                .when(eventService).updateEvent(eventDto, email);

        EventNotFoundException exception =
                assertThrows(EventNotFoundException.class, () -> {
            eventService.updateEvent(eventDto, email);
        });

        assertEquals("Evento non trovato con id "
                + nonExistentEventId, exception.getMessage());

        verify(eventService, times(1)).updateEvent(eventDto, email);
    }

    /**
     * Test per l'eliminazione di un evento inesistente.
     * Deve lanciare un'eccezione EventNotFoundException.
     */
    @Test
    @DisplayName("Eliminazione di un evento inesistente"
            + " -> EventNotFoundException")
    void deleteEventIfNotExistsTest() {
        final long nonExistentEventId = 1999L;
        final String email = "email";

        doThrow(new EventNotFoundException(
                "Evento non trovato con id " + nonExistentEventId))
                .when(eventService).deleteEvent(nonExistentEventId, email);

        EventNotFoundException exception =
                assertThrows(EventNotFoundException.class, () -> {
            eventService.deleteEvent(nonExistentEventId, email);
        });

        assertEquals("Evento non trovato con id "
                + nonExistentEventId, exception.getMessage());

        verify(eventService, times(1)).deleteEvent(nonExistentEventId, email);
    }

    /**
     * Test per il recupero di un evento inesistente.
     * Deve lanciare un'eccezione EventNotFoundException.
     */
    @Test
    @DisplayName("Recupero di un evento inesistente -> EventNotFoundException")
    void getEventByIdAndTerapeutaEmailIfNotExistsTest() {
        final long nonExistentEventId = 1999L;
        final String email = "email";

        when(eventService
                .getEventByIdAndTerapeutaEmail(nonExistentEventId, email))
                .thenThrow(new EventNotFoundException(
                        "Evento non trovato con id " + nonExistentEventId));

        EventNotFoundException exception =
                assertThrows(EventNotFoundException.class, () -> {
            eventService
                    .getEventByIdAndTerapeutaEmail(nonExistentEventId, email);
        });

        assertEquals("Evento non trovato con id "
                + nonExistentEventId, exception.getMessage());

        verify(eventService, times(1))
                .getEventByIdAndTerapeutaEmail(nonExistentEventId, email);
    }

    /**
     * Test per l'aggiunta di un evento.
     * Deve ritornare un EventDto non nullo con l'ID corretto.
     */
    @Test
    @DisplayName("Aggiunta di un evento -> EventDto")
    void addEventTest() {
        final long terapeutaId = 1L;
        final String email = "email";
        final long eventoId = 1L;
        final long timeOffset = 1000L;

        Terapeuta terapeuta = new Terapeuta(
                terapeutaId,
                "nome",
                "cognome",
                email,
                new Date(),
                "password",
                null,
                null,
                null,
                null,
                null
        );

        Evento evento = new Evento(
                eventoId,
                "Evento",
                new Date(),
                new Date(System.currentTimeMillis() + timeOffset),
                terapeuta
        );

        EventDto eventDto = new EventDto(
                eventoId,
                "Evento",
                new Date(),
                new Date(System.currentTimeMillis() + timeOffset)
        );

        when(terapeutaRepository.findByEmail(email))
                .thenReturn(Optional.of(terapeuta));
        when(eventoRespository.save(any(Evento.class))).thenReturn(evento);
        when(modelMapper.map(eventDto, Evento.class)).thenReturn(evento);
        when(modelMapper.map(evento, EventDto.class)).thenReturn(eventDto);

        EventDto result = eventService.addEvent(eventDto, email);

        assertNotNull(result);
        assertEquals(eventoId, result.getId());

        verify(terapeutaRepository, times(1)).findByEmail(email);
        verify(eventoRespository, times(1)).save(any(Evento.class));
        verify(modelMapper, times(1)).map(eventDto, Evento.class);
        verify(modelMapper, times(1)).map(evento, EventDto.class);
    }

    /**
     * Test per l'aggiornamento di un evento esistente.
     * Deve ritornare un EventDto non nullo con l'ID corretto.
     */
    @Test
    @DisplayName("Aggiornamento di un evento -> EventDto")
    void updateEventTest() {
        final long terapeutaId = 1L;
        final long eventoId = 1L;
        final long timeOffset = 1000L;
        final String email = "email";

        Terapeuta terapeuta = new Terapeuta(
                terapeutaId,
                "nome",
                "cognome",
                email,
                new Date(),
                "password",
                null,
                null,
                null,
                null,
                null
        );

        Evento evento = new Evento(
                eventoId,
                "Evento",
                new Date(),
                new Date(System.currentTimeMillis() + timeOffset),
                terapeuta
        );

        EventDto eventDto = new EventDto(
                eventoId,
                "Evento",
                new Date(),
                new Date(System.currentTimeMillis() + timeOffset)
        );

        when(eventoRespository.findById(eventoId))
                .thenReturn(Optional.of(evento));
        when(eventoRespository.save(any(Evento.class))).thenReturn(evento);
        when(modelMapper.map(eventDto, Evento.class)).thenReturn(evento);
        when(modelMapper.map(evento, EventDto.class)).thenReturn(eventDto);

        EventDto result = eventService.updateEvent(eventDto, email);

        assertNotNull(result);
        assertEquals(eventoId, result.getId());

        verify(eventoRespository, times(1)).findById(eventoId);
        verify(eventoRespository, times(1)).save(any(Evento.class));
        verify(modelMapper, times(1)).map(eventDto, Evento.class);
        verify(modelMapper, times(1)).map(evento, EventDto.class);
    }

    /**
     * Test per l'eliminazione di un evento esistente.
     * Deve verificare che il metodo delete sia stato chiamato una volta.
     * Inoltre, dopo l'eliminazione, il recupero
     * dell'evento deve lanciare un'eccezione.
     */
    @Test
    @DisplayName("Eliminazione di un evento -> EventDto")
    void deleteEventTest() {
        final long terapeutaId = 1L;
        final long eventoId = 1L;
        final String email = "email";
        final long timeOffset = 1000L;

        Terapeuta terapeuta = new Terapeuta(
                terapeutaId,
                "nome",
                "cognome",
                email,
                new Date(),
                "password",
                null,
                null,
                null,
                null,
                null
        );

        Evento evento = new Evento(
                eventoId,
                "Evento",
                new Date(),
                new Date(System.currentTimeMillis() + timeOffset),
                terapeuta
        );

        when(eventoRespository.findByIdAndTerapeutaEmail(eventoId, email))
                .thenReturn(Optional.of(evento));

        // Esegui l'eliminazione
        eventService.deleteEvent(eventoId, email);

        // Verifica che il metodo delete sia stato
        // chiamato una volta con l'evento corretto
        verify(eventoRespository, times(1)).delete(evento);

        // Configura il mock per il recupero dell'evento dopo l'eliminazione
        when(eventService.getEventByIdAndTerapeutaEmail(eventoId, email))
                .thenThrow(new EventNotFoundException(
                        "Evento non trovato con id " + eventoId));

        // Verifica che il recupero dell'evento
        // dopo l'eliminazione lanci l'eccezione
        EventNotFoundException exception =
                assertThrows(EventNotFoundException.class, () -> {
            eventService.getEventByIdAndTerapeutaEmail(eventoId, email);
        });

        assertEquals("Evento non trovato con id "
                + eventoId, exception.getMessage());

        verify(eventService, times(1))
                .getEventByIdAndTerapeutaEmail(eventoId, email);
    }
}
