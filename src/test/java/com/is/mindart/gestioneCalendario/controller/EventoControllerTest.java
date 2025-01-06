package com.is.mindart.gestioneCalendario.controller;

import com.is.mindart.gestioneCalendario.service.EventDto;
import com.is.mindart.gestioneCalendario.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class EventoControllerTest {

    /**
     * Mock del servizio per la gestione degli eventi.
     */
    @Mock
    private EventService eventService;

    /**
     * Controller per la gestione degli eventi,
     * iniettato con il mock del servizio.
     */
    @InjectMocks
    private EventoController eventoController;

    /**
     * Mock dell'oggetto di autenticazione.
     */
    @Mock
    private Authentication authentication;

    /**
     * Inizializza i mock e il controller prima di ogni test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn("terapeuta@example.com");
    }

    /**
     * Test per il metodo {@link EventoController#getAllEvents()}.
     */
    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testGetAllEvents() {
        List<EventDto> events = List.of(new EventDto());
        when(eventService
                .getAllEvents("terapeuta@example.com")).thenReturn(events);

        ResponseEntity<List<EventDto>> response =
                eventoController.getAllEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(events, response.getBody());
    }

    /**
     * Test per il metodo {@link EventoController#getEventById(Long)}.
     */
    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testGetEventById() {
        EventDto event = new EventDto();
        when(eventService
                .getEventByIdAndTerapeutaEmail(1L, "terapeuta@example.com"))
                .thenReturn(event);

        ResponseEntity<EventDto> response = eventoController.getEventById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    /**
     * Test per il metodo {@link EventoController#updateEvent(EventDto)}.
     */
    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testUpdateEvent() {
        EventDto event = new EventDto();
        when(eventService
                .updateEvent(event, "terapeuta@example.com")).thenReturn(event);

        ResponseEntity<EventDto> response = eventoController.updateEvent(event);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    /**
     * Test per il metodo {@link EventoController#deleteEvent(Long)}.
     */
    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testDeleteEvent() {
        doNothing().when(eventService).deleteEvent(1L, "terapeuta@example.com");

        ResponseEntity<Void> response = eventoController.deleteEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test per il metodo {@link EventoController#addEvent(EventDto)}.
     */
    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testAddEvent() {
        EventDto event = new EventDto();
        when(eventService
                .addEvent(event, "terapeuta@example.com")).thenReturn(event);

        ResponseEntity<EventDto> response = eventoController.addEvent(event);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(event, response.getBody());
    }
}
