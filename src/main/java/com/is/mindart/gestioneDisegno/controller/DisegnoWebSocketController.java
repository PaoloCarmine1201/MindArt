package com.is.mindart.gestioneDisegno.controller;

import com.is.mindart.gestioneDisegno.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.NoSuchElementException;

@Controller
public class DisegnoWebSocketController {
    /**
     * Il template per l'invio di messaggi via WebSocket.
     */
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Il servizio per la gestione dei messaggi di disegno.
     */
    @Autowired
    private DisegnoService disegnoSocketService;

    /**
     * Riceve i messaggi di disegno inviati dai client via WebSocket.
     *
     * @param drawingMessage Il messaggio di disegno.
     */
    @MessageMapping("/draw")
    public void handleDrawMessage(@Payload final DisegnoMessage drawingMessage) {
        try {
            // Aggiorna il disegno tramite il servizio dedicato
            DisegnoResponseDTO savedMessage = disegnoSocketService.updateDisegnoViaSocket(drawingMessage);

            if (savedMessage != null) {
                // Invia il disegno aggiornato a tutti i client nella stessa sessione
                messagingTemplate.convertAndSend(String.valueOf(drawingMessage.getBambinoId()), savedMessage);
            }
        } catch (NoSuchElementException e) {
            // Gestione dell'errore: Disegno o Sessione non trovati
            messagingTemplate.convertAndSendToUser(String.valueOf(drawingMessage.getBambinoId()), "/queue/errors", "Disegno o Sessione non trovati.");
        } catch (Exception e) {
            // Gestione di altri errori
            e.printStackTrace();
            messagingTemplate.convertAndSendToUser(String.valueOf(drawingMessage.getBambinoId()), "/queue/errors", "Errore durante l'aggiornamento del disegno.");
        }
    }

    /**
     * Gestisce l'unione di un utente a una sessione.
     *
     * @param sessionMessage Il messaggio di unione.
     */
    @MessageMapping("/join")
    public void handleJoinMessage(@Payload final SessioneMessage sessionMessage) {
        try {
            // Recupera lo storico del disegno della sessione tramite un metodo dedicato
            DisegnoResponseDTO disegno = disegnoSocketService.getDisegnoBySessioneId(sessionMessage.getSessionId());
            // Invia lo storico al nuovo utente
            messagingTemplate.convertAndSendToUser(String.valueOf(sessionMessage.getBambinoId()), "/queue/history", disegno);
        } catch (NoSuchElementException e) {
            // Se non esiste un Disegno per la sessione, invia una risposta vuota
            messagingTemplate.convertAndSendToUser(String.valueOf(sessionMessage.getBambinoId()), "/queue/history", null);
        } catch (Exception e) {
            // Gestione degli errori
            e.printStackTrace();
            messagingTemplate.convertAndSendToUser(String.valueOf(sessionMessage.getBambinoId()), "/queue/errors", "Errore durante il recupero dello storico del disegno.");
        }
    }
}
