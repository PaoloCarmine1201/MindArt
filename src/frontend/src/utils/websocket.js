// src/services/websocket.js

import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

/**
 * Recupera il token JWT dal localStorage o da un'altra fonte sicura.
 * Assicurati che il token sia stato memorizzato al momento del login.
 */
const getJwtToken = () => {
    return localStorage.getItem('jwtToken'); // Adatta questo metodo secondo le tue esigenze
};

/**
 * Funzione per connettersi al WebSocket con autenticazione JWT
 */
export const connectWebSocket = () => {
    const token = getJwtToken();

    const client = new Client({
        // Non impostare brokerURL quando usi webSocketFactory
        // brokerURL: 'ws://localhost:8080/ws', // Rimosso per evitare conflitti

        // Utilizza webSocketFactory per SockJS
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),

        // Header di connessione, includendo il token JWT
        connectHeaders: {
            'Authorization': `Bearer ${token}`, // Includi il token JWT
        },

        // Debugging: stampa i messaggi di debug nella console
        debug: function (str) {
            console.log(str);
        },

        // Tentativo di riconnessione ogni 5 secondi in caso di disconnessione
        reconnectDelay: 5000,

        // Heartbeats per mantenere attiva la connessione
        heartbeatIncoming: 4000,  // Tempo in millisecondi per ricevere heartbeat dal server
        heartbeatOutgoing: 4000,  // Tempo in millisecondi per inviare heartbeat al server

        /**
         * Callback quando la connessione viene stabilita con successo
         */
        onConnect: function (frame) {
            console.log('Connesso al WebSocket: ', frame);
            // Puoi aggiungere ulteriori logiche qui, se necessario
        },

        /**
         * Callback per gestire errori di connessione
         */
        onStompError: function (frame) {
            console.error('Errore STOMP: ', frame.headers['message']);
            console.error('Dettagli: ', frame.body);
        },

        /**
         * Callback quando la connessione viene chiusa
         */
        onDisconnect: function (frame) {
            console.log('Disconnesso dal WebSocket: ', frame);
        },
    });

    // Attiva il client STOMP
    client.activate();

    return client;
};

/**
 * Funzione per sottoscriversi a un topic specifico e gestire i messaggi ricevuti
 * @param {Client} client - Istanza del client STOMP
 * @param {number|string} disegnoId - ID del Disegno a cui sottoscriversi
 * @param {function} onMessageReceived - Callback da eseguire quando si riceve un messaggio
 * @returns {Subscription} - Oggetto di sottoscrizione, utile per annullare la sottoscrizione se necessario
 */
export const subscribeToDraw = (client, disegnoId, onMessageReceived) => {
    if (!client || !client.connected) {
        console.error('Il client STOMP non è connesso.');
        return null;
    }

    const subscription = client.subscribe(`/topic/draw/${disegnoId}`, (message) => {
        try {
            const body = JSON.parse(message.body);
            onMessageReceived(body);
        } catch (error) {
            console.error('Errore nella parsing del messaggio: ', error);
        }
    });

    return subscription;
};

/**
 * Funzione per disconnettersi dal WebSocket
 * @param {Client} client - Istanza del client STOMP
 */
export const disconnectWebSocket = (client) => {
    if (client && client.active) {
        client.deactivate();
    }
};
