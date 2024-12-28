// src/services/websocket.js

import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

// Funzione per connettersi al WebSocket
export const connectWebSocket = () => {
    const client = new Client({
        brokerURL: 'ws://localhost:8080/ws', // URL del WebSocket endpoint
        connectHeaders: {
            // Se hai bisogno di autenticazione, aggiungi qui
            // login: 'user',
            // passcode: 'password',
        },
        debug: function (str) {
            console.log(str);
        },
        reconnectDelay: 5000, // Tentativo di riconnessione ogni 5 secondi
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
    });

    client.activate();

    return client;
};

// Funzione per sottoscriversi a un topic specifico e gestire i messaggi ricevuti
export const subscribeToDraw = (client, disegnoId, onMessageReceived) => {
    const subscription = client.subscribe(`/topic/draw/${disegnoId}`, (message) => {
        const body = JSON.parse(message.body);
        onMessageReceived(body);
    });

    return subscription;
};
