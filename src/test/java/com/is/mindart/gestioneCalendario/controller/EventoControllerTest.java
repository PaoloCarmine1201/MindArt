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
import static org.mockito.Mockito.*;

public class EventoControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventoController eventoController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn("terapeuta@example.com");
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testGetAllEvents() {
        List<EventDto> events = List.of(new EventDto());
        when(eventService.getAllEvents("terapeuta@example.com")).thenReturn(events);

        ResponseEntity<List<EventDto>> response = eventoController.getAllEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(events, response.getBody());
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testGetEventById() {
        EventDto event = new EventDto();
        when(eventService.getEventByIdAndTerapeutaEmail(1L, "terapeuta@example.com")).thenReturn(event);

        ResponseEntity<EventDto> response = eventoController.getEventById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testUpdateEvent() {
        EventDto event = new EventDto();
        when(eventService.updateEvent(event, "terapeuta@example.com")).thenReturn(event);

        ResponseEntity<EventDto> response = eventoController.updateEvent(event);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testDeleteEvent() {
        doNothing().when(eventService).deleteEvent(1L, "terapeuta@example.com");

        ResponseEntity<Void> response = eventoController.deleteEvent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(roles = "TERAPEUTA")
    public void testAddEvent() {
        EventDto event = new EventDto();
        when(eventService.addEvent(event, "terapeuta@example.com")).thenReturn(event);

        ResponseEntity<EventDto> response = eventoController.addEvent(event);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(event, response.getBody());
    }
}
