package com.is.mindart.gestioneCalendario.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EventDtoTest {

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
}
