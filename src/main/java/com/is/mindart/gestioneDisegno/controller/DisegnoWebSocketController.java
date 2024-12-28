package com.is.mindart.gestioneDisegno.controller;

import com.is.mindart.gestioneDisegno.service.DisegnoService;
import com.is.mindart.gestioneDisegno.service.StrokeDTO;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class DisegnoWebSocketController {

    /**
     * Servizio per la gestione dei Disegni
     */
    private final DisegnoService disegnoService;

    /**
     * Gestisce un nuovo stroke inviato da un client.
     * @param disegnoId ID del Disegno a cui appartiene lo stroke
     * @param strokeDto DTO dello stroke inviato dal client
     * @return Lo stesso DTO dello stroke, da inviare in broadcast a tutti i client connessi a /topic/draw/{disegnoId}
     */
    @MessageMapping("/draw/{disegnoId}")
    @SendTo("/topic/draw/{disegnoId}")
    public StrokeDTO handleNewStroke(
             final @DestinationVariable("disegnoId") Long disegnoId,
             final @Payload StrokeDTO strokeDto) {
        System.out.println("Ricevuto nuovo stroke: " + strokeDto);

        // Aggiorno il Disegno sul DB
        disegnoService.addStrokeToDisegno(disegnoId, strokeDto);

        // Ritorno lo stroke in broadcast a tutti i client connessi a /topic/draw/{disegnoId}
        return strokeDto;
    }
}

