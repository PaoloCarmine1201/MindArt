// src/components/DrawingBoard.jsx

import React, { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Line, Rect } from 'react-konva';
import { connectWebSocket, subscribeToDraw } from "../../utils/websocket";
import axiosInstance from "../../config/axiosInstance";
import "../../style/Lavagna.css"; // Import del file CSS

const DrawingBoard = () => {
    const [actions, setActions] = useState([]); // Unico array per tutte le azioni
    const [disegnoId, setDisegnoId] = useState(null); // ID del disegno
    const stageRef = useRef(null);
    const stompClientRef = useRef(null);

    // State for Stage dimensions
    const [dimensions, setDimensions] = useState({
        width: window.innerWidth,
        height: window.innerHeight,
    });

    // Drawing area offsets and dimensions based on state
    const DRAWING_AREA_OFFSET_X = 25; // Offset orizzontale (largo quanto il selettore)
    const DRAWING_AREA_OFFSET_Y = 25; // Offset verticale (per lasciare un margine in alto)
    const DRAWING_AREA_WIDTH = dimensions.width - 50; // Larghezza dell'area di disegno
    const DRAWING_AREA_HEIGHT = dimensions.height - DRAWING_AREA_OFFSET_Y - 50; // Altezza dell'area di disegno

    // Funzione per formattare i punti
    const formatPoints = (points) => {
        if (Array.isArray(points) && points.length > 0) {
            // Verifica se i punti sono annidati, ad esempio [[x1, y1], [x2, y2], ...]
            if (Array.isArray(points[0])) {
                return points.flat();
            }
            // Se i punti sono già in formato piatto [x1, y1, x2, y2, ...]
            return points;
        }
        return [];
    };

    // Effetto per gestire il caricamento iniziale dei dati
    useEffect(() => {
        const loadActions = async () => {
            try {
                const disegnoResponse = await axiosInstance.get(`/api/terapeuta/sessione/disegno/`);
                const disegno = disegnoResponse.data;
                setDisegnoId(disegno.id);

                // Formattazione delle azioni (strokes e filledAreas)
                const initialStrokes = disegno.strokes.map(stroke => ({
                    ...stroke,
                    type: stroke.type || "stroke",
                    points: formatPoints(stroke.points),
                }));

                const initialFilledAreas = (disegno.filledAreas || []).map(area => ({
                    ...area,
                    type: area.type || "lasso",
                    points: formatPoints(area.points),
                }));

                const initialActions = [...initialStrokes, ...initialFilledAreas];
                setActions(initialActions);

                console.log('Actions loaded:', initialActions);
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
                // Gestisci l'errore come necessario, ad esempio reindirizzando l'utente
            }
        };

        loadActions();
    }, []);

    // Effetto per gestire la connessione WebSocket una volta ottenuto disegnoId
    useEffect(() => {
        if (!disegnoId) return; // Attendi che disegnoId sia disponibile

        const client = connectWebSocket();

        client.onConnect = () => {
            console.log('Connesso al WebSocket');
            subscribeToDraw(client, disegnoId, (data) => {
                const formattedAction = {
                    ...data,
                    points: formatPoints(data.points), // Formatta i punti
                };
                setActions((prevActions) => [...prevActions, formattedAction]);
                console.log('Received action via WebSocket:', formattedAction);
            });
        };

        client.onStompError = (frame) => {
            console.error('Errore STOMP:', frame.headers['message']);
            // Puoi gestire errori STOMP qui, ad esempio mostrando un messaggio all'utente
        };

        client.activate(); // Attiva il client STOMP

        stompClientRef.current = client;

        // Cleanup alla disconnessione
        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [disegnoId]);

    // Gestione del resize della finestra
    useEffect(() => {
        const handleResize = () => {
            setDimensions({
                width: window.innerWidth,
                height: window.innerHeight,
            });
        };

        window.addEventListener('resize', handleResize);
        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    // Effetto per loggare lo stato corrente di actions (per debug)
    useEffect(() => {
        console.log('Current actions:', actions);
    }, [actions]);

    return (
        <div>
            <Stage
                width={dimensions.width}
                height={dimensions.height}
                ref={stageRef}
                style={{
                    backgroundColor: "#f0f0f0",
                    cursor: "default",
                }}
            >
                <Layer name="drawing-layer">
                    {/* Rettangolo visibile per l'area di disegno */}
                    <Rect
                        x={DRAWING_AREA_OFFSET_X}
                        y={DRAWING_AREA_OFFSET_Y}
                        width={DRAWING_AREA_WIDTH}
                        height={DRAWING_AREA_HEIGHT}
                        fill="#ffffff" // Colore di sfondo dell'area di disegno
                        stroke="black" // Colore del bordo
                        strokeWidth={2} // Spessore del bordo
                        cornerRadius={15} // Arrotonda i bordi del rettangolo
                        shadowColor="black" // Colore dell'ombra
                        shadowBlur={10} // Intensità della sfumatura dell'ombra
                        shadowOffset={{x: 5, y: 5}} // Spostamento dell'ombra
                        shadowOpacity={0.2} // Opacità dell'ombra
                    />

                    {/* Itera su tutte le azioni */}
                    {actions.map((action, index) => {
                        if (action.type === "lasso") {
                            return (
                                <Line
                                    key={`action-${index}`}
                                    points={action.points} // Punti già formattati
                                    closed={true}
                                    fill={action.color}
                                    stroke={action.color}
                                    strokeWidth={1}
                                    opacity={0.5}
                                />
                            );
                        } else if (action.type === "stroke") {
                            return (
                                <Line
                                    key={`action-${index}`}
                                    points={action.points} // Punti già formattati
                                    stroke={action.color}
                                    strokeWidth={action.strokeWidth}
                                    lineCap="round"
                                    lineJoin="round"
                                    tension={0.5}
                                    globalCompositeOperation={action.color === "white" ? 'destination-out' : 'source-over'}
                                />
                            );
                        } else {
                            return null; // In caso di tipi sconosciuti
                        }
                    })}
                </Layer>
            </Stage>
        </div>
    );
};

export default DrawingBoard;
