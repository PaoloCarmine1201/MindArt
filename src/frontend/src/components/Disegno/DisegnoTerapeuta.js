// src/components/DrawingBoard.jsx

import React, { useEffect, useRef, useState } from 'react';
import { Stage, Layer, Line, Rect, Image as KonvaImage } from 'react-konva';
import useImage from 'use-image'; // Hook per caricare immagini
import { connectWebSocket, subscribeToDraw } from "../../utils/websocket";
import axiosInstance from "../../config/axiosInstance";
import "../../style/Lavagna.css"; // Import del file CSS
import { useNavigate } from 'react-router-dom'; // Importa useNavigate per navigare
// Helper function to map file extensions to MIME types
const getMimeType = (filename) => {
    const extension = filename.split('.').pop().toLowerCase();
    const mimeTypes = {
        'png': 'image/png',
        'jpg': 'image/jpeg',
        'jpeg': 'image/jpeg',
        'gif': 'image/gif',
        'svg': 'image/svg+xml',
        'bmp': 'image/bmp',
        'webp': 'image/webp',
        // Aggiungi altre estensioni se necessario
    };
    return mimeTypes[extension] || 'image/png'; // Default MIME type
};

const DrawingBoard = () => {
    const [actions, setActions] = useState([]); // Array unico per tutte le azioni
    const [disegnoId, setDisegnoId] = useState(null); // ID del disegno
    const stageRef = useRef(null);
    const stompClientRef = useRef(null);
    const navigate = useNavigate(); // Hook per la navigazione

    // Dimensioni fisse per il Stage e l'area di disegno
    const STAGE_WIDTH = 1920;
    const STAGE_HEIGHT = 1080;
    const DRAWING_AREA_OFFSET_X = 25; // Offset orizzontale
    const DRAWING_AREA_OFFSET_Y = 25; // Offset verticale
    const DRAWING_AREA_WIDTH = 1870; // Larghezza fissa dell'area di disegno
    const DRAWING_AREA_HEIGHT = 1030; // Altezza fissa dell'area di disegno

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

    // Stato per l'immagine di sfondo in base64
    const [backgroundImage, setBackgroundImage] = useState(null);
    const [bgImage] = useImage(backgroundImage);

    // Effetto per caricare il disegno e l'immagine di sfondo
    useEffect(() => {
        const loadActions = async () => {
            try {
                const disegnoResponse = await axiosInstance.get(`/api/terapeuta/sessione/disegno/`);
                const disegno = disegnoResponse.data;
                setDisegnoId(disegno.id);

                console.log('Disegno caricato:', disegno);



                // Gestisci strokes e filledAreas
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
                console.error('Errore nel caricamento delle azioni:', error);
                navigate('/'); // Reindirizza alla home page in caso di errore
            }
        };

        const loadBackgroundImage = async () => {
            try {
                // Carica l'immagine di sfondo associata alla sessione
                const materialeResponse = await axiosInstance.get(`/api/terapeuta/materiale/sessione/`, {
                    responseType: 'json',
                });
                console.log('Materiale caricato:', materialeResponse.data);

                if (materialeResponse.data && materialeResponse.data.file && materialeResponse.data.nome) {
                    const base64Image = materialeResponse.data.file;
                    const nomeFile = materialeResponse.data.nome;

                    // Ottieni il tipo MIME basato sull'estensione del nome del file
                    const mimeType = getMimeType(nomeFile);

                    // Costruisci il data URL
                    const imageUrl = `data:${mimeType};base64,${base64Image}`;
                    console.log('URL dell\'immagine di sfondo:', imageUrl);

                    setBackgroundImage(imageUrl);
                }
            } catch (error) {
                console.error('Errore nel caricamento dell\'immagine di sfondo:', error);
            }
        }

        loadActions();
        loadBackgroundImage();
    }, [navigate]);

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

    // Effetto per loggare lo stato corrente di actions (per debug)
    useEffect(() => {
        console.log('Current actions:', actions);
    }, [actions]);

    return (
        <>

            <div className="drawing-container">
                <Stage
                    width={STAGE_WIDTH}
                    height={STAGE_HEIGHT}
                    ref={stageRef}
                    style={{
                        backgroundColor: "#f0f0f0",
                        cursor: "default",
                    }}
                >
                    {/* Layer 1: Rettangolo dell'area di disegno */}
                    <Layer name="rect-layer">
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
                            shadowOffset={{ x: 5, y: 5 }} // Spostamento dell'ombra
                            shadowOpacity={0.2} // Opacità dell'ombra
                        />
                    </Layer>

                    {/* Layer 2: Immagine di sfondo */}
                    {bgImage && (
                        <Layer name="background-layer">
                            <KonvaImage
                                image={bgImage}
                                x={DRAWING_AREA_OFFSET_X}
                                y={DRAWING_AREA_OFFSET_Y}
                                width={DRAWING_AREA_WIDTH}
                                height={DRAWING_AREA_HEIGHT}
                                listening={false} // Disabilita gli eventi per il layer di sfondo
                                onError={() => {
                                    console.error('Errore nel caricamento dell\'immagine di sfondo');
                                }}
                            />
                        </Layer>
                    )}

                    {/* Layer 3: Tratti e Lasso */}
                    <Layer name="drawing-layer">
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
        </>
    );
};

export default DrawingBoard;
