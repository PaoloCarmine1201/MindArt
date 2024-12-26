// src/components/DrawingBoard.js

import React, { useState, useEffect, useRef } from 'react';
import { Stage, Layer, Line } from 'react-konva';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { debounce } from 'lodash';

const DrawingBoard = ({ sessionId, childId }) => { // Rimosso bambinoIds per semplificazione
    const [color, setColor] = useState('#FF0000'); // Colore predefinito
    const [lines, setLines] = useState([]);        // Lista di tratti
    const [currentLine, setCurrentLine] = useState(null); // Tratto in corso di disegno
    const stompClientRef = useRef(null);

    useEffect(() => {
        // Connessione WebSocket
        const socket = new SockJS('http://localhost:8080/ws-drawing'); // Assicurati che l'URL sia corretto
        const client = Stomp.over(socket);
        client.connect({}, () => {
            // Sottoscrivi al topic della sessione
            client.subscribe(`/topic/draw/${sessionId}`, (message) => {
                const disegno = JSON.parse(message.body);
                if (disegno && disegno.disegno && disegno.disegno.strokes) {
                    setLines(disegno.disegno.strokes);
                }
            });

            // Invio messaggio di join
            client.send('/app/join', {}, JSON.stringify({ sessionId, childId }));
        }, (error) => {
            console.error("Errore di connessione WebSocket:", error);
        });

        stompClientRef.current = client;

        // Recupera lo storico dei disegni quando il componente si monta
        /*fetch(`http://localhost:8080/api/bambino/sessione/${sessionId}`)
            .then(response => response.json())
            .then(data => {
                if (data && data.disegno && data.disegno.strokes) {
                    setLines(data.disegno.strokes);
                }
            })
            .catch(error => console.error("Errore nel recupero dello storico dei disegni:", error));
*/
        return () => {
            client.disconnect();
        };
    }, [sessionId, childId]);

    // Funzione di debounced per inviare i tratti
    const sendDrawingMessage = useRef(debounce((message) => {
        stompClientRef.current.send('/app/draw', {}, JSON.stringify(message));
    }, 100)); // Invio ogni 100ms

    const handleMouseDown = (e) => {
        const pos = e.target.getStage().getPointerPosition();
        setCurrentLine({ points: [pos.x, pos.y], color, thickness: 2 });
    };

    const handleMouseMove = (e) => {
        if (!currentLine) return;
        const pos = e.target.getStage().getPointerPosition();
        const newPoints = currentLine.points.concat([pos.x, pos.y]);
        setCurrentLine({ ...currentLine, points: newPoints });

        // Invia messaggio debounced con i nuovi punti
        sendDrawingMessage.current({
            sessionId,
            childId,
            points: [pos.x, pos.y],
            color: currentLine.color,
        });
    };

    const handleMouseUp = () => {
        if (currentLine) {
            setLines([...lines, {
                points: currentLine.points,
                color: currentLine.color,
                thickness: currentLine.thickness,
            }]);
            // Invia un messaggio finale per segnalare la fine del tratto
            stompClientRef.current.send('/app/draw', {}, JSON.stringify({
                sessionId,
                childId,
                points: [],                // Campo vuoto per indicare la fine del tratto
                color: currentLine.color,
            }));
            setCurrentLine(null);
        }
    };

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
                            cursor: 'pointer'
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
                    {/* Renderizza tutti i tratti */}
                    {lines.map((line, i) => (
                        <Line
                            key={i}
                            points={line.points}
                            stroke={line.color}
                            strokeWidth={line.thickness}
                            tension={0.5}
                            lineCap="round"
                            globalCompositeOperation="source-over"
                        />
                    ))}
                    {/* Renderizza il tratto in corso */}
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
