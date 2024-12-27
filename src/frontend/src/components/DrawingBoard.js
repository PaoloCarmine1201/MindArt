// src/components/DrawingBoard.js

import React, { useState, useEffect, useRef } from 'react';
import { Stage, Layer, Line, Text } from 'react-konva';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { debounce } from 'lodash';
import axiosInstance from "../config/axiosInstance";


const POINTS_BATCH_SIZE = 10; // Numero di punti da accumulare prima di inviare

const DrawingBoard = ({ sessionId, bambinoId }) => {
    const [color, setColor] = useState('#FF0000'); // Colore predefinito
    const [lines, setLines] = useState([]); // Lista di tratti
    const [currentLine, setCurrentLine] = useState(null); // Tratto in corso di disegno
    const [loading, setLoading] = useState(true); // Stato di caricamento
    const [error, setError] = useState(null); // Stato di errore
    const stompClientRef = useRef(null);
    const pointsBufferRef = useRef([]); // Buffer per accumulare i punti

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws-drawing'); // Cambia l'URL secondo le tue necessità
        const client = Stomp.over(socket);

        // Opzionale: Disabilita il logging di STOMP per evitare rumore nei log
        client.debug = () => {};

        client.connect(
            {}, // Le intestazioni di autenticazione sono gestite tramite l'interceptor di Axios
            () => {
                console.log('Connesso a STOMP');

                // Sottoscrivi al topic della sessione
                client.subscribe(`/topic/draw/${sessionId}`, (message) => {
                    const disegno = JSON.parse(message.body);
                    console.log('Disegno ricevuto dal server:', disegno); // Log per verificare i dati ricevuti
                    if (disegno && disegno.disegno && disegno.disegno.strokes) {
                        setLines(disegno.disegno.strokes);
                        console.log('Linee aggiornate:', disegno.disegno.strokes);
                    } else {
                        console.warn('La risposta dal server non contiene linee valide.');
                    }
                });

                // Invio messaggio di join
                client.send('/app/join', {}, JSON.stringify({ sessionId, bambinoId }));
            },
            (error) => {
                console.error('Errore di connessione WebSocket:', error);
                alert('Errore di connessione WebSocket. Controlla le credenziali e riprova.');
            }
        );

        stompClientRef.current = client;

        // Recupera lo storico dei disegni quando il componente si monta
        const fetchHistoricalDrawings = async () => {
            try {
                const response = await axiosInstance.get(`/api/bambino/sessione/${sessionId}`);
                console.log('Risposta storico disegni:', response.data); // Log per verificare i dati ricevuti
                const data = response.data;
                if (data && data.disegno && data.disegno.strokes) {
                    setLines(data.disegno.strokes);
                    console.log('Linee impostate:', data.disegno.strokes);
                } else {
                    console.warn('La risposta non contiene dati validi per le linee.');
                    setError('Nessun dato di disegno trovato.');
                }
            } catch (error) {
                console.error('Errore nel recupero dello storico dei disegni:', error);
                setError('Errore nel recupero dei dati di disegno.');
            } finally {
                setLoading(false);
            }
        };

        fetchHistoricalDrawings();

        return () => {
            if (client && client.connected) {
                client.disconnect(() => {
                    console.log('Disconnesso da STOMP');
                });
            }
        };
    }, [sessionId, bambinoId]);

    // Funzione di debounced per inviare i tratti
    const sendDrawingMessage = useRef(
        debounce((message) => {
            console.log('Invio messaggio di disegno:', message); // Log per verificare i dati inviati
            if (stompClientRef.current && stompClientRef.current.connected) {
                stompClientRef.current.send('/app/draw', {}, JSON.stringify(message));
            }
        }, 2000) // Puoi regolare il debounce secondo le tue necessità
    ).current;

    const handleMouseDown = (e) => {
        const pos = e.target.getStage().getPointerPosition();
        setCurrentLine({ points: [pos.x, pos.y], color, thickness: 2 });
        console.log('Tratto iniziato con punti:', [pos.x, pos.y]);
    };

    const handleMouseMove = (e) => {
        if (!currentLine) return;
        const pos = e.target.getStage().getPointerPosition();
        const newPoints = currentLine.points.concat([pos.x, pos.y]);
        setCurrentLine({ ...currentLine, points: newPoints });

        // Aggiungi il punto al buffer
        pointsBufferRef.current.push(pos.x, pos.y);
        console.log('Buffer attuale:', pointsBufferRef.current); // Log per monitorare il buffer

        // Controlla se il buffer ha raggiunto la dimensione desiderata
        if (pointsBufferRef.current.length >= POINTS_BATCH_SIZE * 2) {
            const pointsToSend = [...pointsBufferRef.current];
            pointsBufferRef.current = []; // Resetta il buffer

            // Verifica che i punti siano in coppie
            if (pointsToSend.length % 2 !== 0) {
                console.error('Numero di punti dispari. I punti devono essere in coppie (x, y).');
                return;
            }

            // Invia i punti accumulati al backend
            sendDrawingMessage({
                sessionId,
                userId: bambinoId,
                points: pointsToSend,
                color: currentLine.color,
                thickness: 2,
            });
        }
    };

    const handleMouseUp = () => {
        if (currentLine) {
            setLines([...lines, {
                points: currentLine.points,
                color: currentLine.color,
                thickness: currentLine.thickness,
            }]);
            console.log('Tratto aggiunto allo stato:', currentLine);

            // Invia eventuali punti rimanenti nel buffer
            if (pointsBufferRef.current.length > 0) {
                // Verifica che i punti siano in coppie
                if (pointsBufferRef.current.length % 2 !== 0) {
                    console.error('Numero di punti dispari. I punti devono essere in coppie (x, y).');
                } else {
                    sendDrawingMessage({
                        sessionId,
                        userId: bambinoId,
                        points: [...pointsBufferRef.current],
                        color: currentLine.color,
                        thickness: 2,
                    });
                    console.log('Messaggio di punti finali inviato:', {
                        sessionId,
                        userId: bambinoId,
                        points: [...pointsBufferRef.current],
                        color: currentLine.color,
                        thickness: 2,
                    });
                }
                pointsBufferRef.current = []; // Resetta il buffer
            }

            // Invia un messaggio finale per segnalare la fine del tratto
            sendDrawingMessage({
                sessionId,
                userId: bambinoId,
                color: currentLine.color,
                thickness: 2,
            });
            console.log('Messaggio finale per la fine del tratto inviato.');

            setCurrentLine(null);
        }
    };

    if (loading) return <div>Caricamento disegni...</div>;
    if (error) return <div style={{ color: 'red' }}>{error}</div>;

    return (
        <div>
            {/* Palette di Colori */}
            <div className="palette" style={{ marginBottom: '10px' }}>
                {['#FF0000', '#00FF00', '#0000FF', '#FFFF00', '#FF00FF', '#000000'].map((c) => (
                    <button
                        key={c}
                        style={{
                            backgroundColor: c,
                            width: '30px',
                            height: '30px',
                            border: color === c ? '3px solid #000' : '1px solid #ccc',
                            marginRight: '5px',
                            cursor: 'pointer',
                        }}
                        onClick={() => setColor(c)}
                    />
                ))}
            </div>

            {/* Area di Disegno */}
            <Stage
                width={window.innerWidth}
                height={window.innerHeight - 50} // Adatta l'altezza se necessario
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                style={{ border: '1px solid #ddd' }}
            >
                <Layer>
                    {lines.length > 0 ? (
                        lines.map((line, i) => (
                            <Line
                                key={i}
                                points={line.points}
                                stroke={line.color}
                                strokeWidth={line.thickness}
                                tension={0.5}
                                lineCap="round"
                                globalCompositeOperation="source-over"
                            />
                        ))
                    ) : (
                        <Text text="Nessun disegno disponibile." />
                    )}
                    {currentLine && (
                        <Line
                            points={currentLine.points}
                            stroke={currentLine.color}
                            strokeWidth={currentLine.thickness}
                            tension={0.5}
                            lineCap="round"
                            globalCompositeOperation="source-over"
                        />
                    )}
                </Layer>
            </Stage>
        </div>
    );
};

export default DrawingBoard;
