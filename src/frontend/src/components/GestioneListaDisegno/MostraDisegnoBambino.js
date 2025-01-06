import React, { useEffect, useState, useRef } from 'react';
import { Stage, Layer, Line, Image as KonvaImage } from 'react-konva';
import axiosInstance from "../../config/axiosInstance";
import "../../style/Lavagna.css";
import "../../style/LavagnaVisualizzaDisegni.css";
import "../../style/Button.css";
import { Button } from "react-bootstrap";
import ValutazionePopup from "./ValutazionePopup";
import { toast } from "react-toastify";
import useImage from "use-image";

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

const MostraDisegnoBambino = ({ disegnoId, tema }) => {
    const [actions, setActions] = useState([]);
    const stageRef = useRef(null);
    const [showValutazione, setShowValutazione] = useState(false);
    const [valutazione, setValutazione] = useState("");
    const [commento, setCommento] = useState("");

    // Imposta le dimensioni fisse a 1920x1080
    const [dimensions] = useState({
        width: 1920,
        height: 1080,
    });

    const [drawingBounds, setDrawingBounds] = useState({
        minX: 0,
        minY: 0,
        maxX: 1920, // Aggiorna in base alle dimensioni fisse
        maxY: 1080,
    });

    // Stato per l'immagine di sfondo in base64
    const [backgroundImage, setBackgroundImage] = useState(null);
    const [bgImage] = useImage(backgroundImage);

    useEffect(() => {
        const loadActions = async () => {
            try {
                const disegnoResponse = await axiosInstance.get(`/api/terapeuta/disegno/${disegnoId}`);
                const disegno = disegnoResponse.data;

                const initialStrokes = disegno.strokes.map(stroke => ({
                    ...stroke,
                    type: stroke.type || "stroke",
                    points: stroke.points.flat(),
                }));

                const initialFilledAreas = (disegno.filledAreas || []).map(area => ({
                    ...area,
                    type: area.type || "lasso",
                    points: area.points.flat(),
                }));

                const initialActions = [...initialStrokes, ...initialFilledAreas];
                setActions(initialActions);

                // Calcola i limiti del disegno
                const allPoints = initialActions.flatMap(action => action.points);
                const minX = Math.min(...allPoints.filter((_, index) => index % 2 === 0));
                const minY = Math.min(...allPoints.filter((_, index) => index % 2 === 1));
                const maxX = Math.max(...allPoints.filter((_, index) => index % 2 === 0));
                const maxY = Math.max(...allPoints.filter((_, index) => index % 2 === 1));

                setDrawingBounds({
                    minX: Math.min(minX, 0),
                    minY: Math.min(minY, 0),
                    maxX: Math.max(maxX, 1920),
                    maxY: Math.max(maxY, 1080),
                });
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
            }
        };

        const loadBackground = async () => {
            // Carica l'immagine di sfondo associata alla sessione
            const materialeResponse = await axiosInstance.get(`/api/terapeuta/materiale/disegno/${disegnoId}`, {
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
        }

        if (disegnoId) {
            loadActions();
            loadBackground();
        }
    }, [disegnoId]); // Rimosso dimensions.width e dimensions.height dalle dipendenze

    // Rimosso l'useEffect per il ridimensionamento della finestra

    const handleSubmitValutazione = async () => {
        try {
            await axiosInstance.post(`/api/terapeuta/disegno/${disegnoId}/valutazione`, {
                valutazione,
            });

            toast.success("Valutazione inviata con successo!");
            setShowValutazione(false);
            window.location.reload(); // Ricarica la pagina per visualizzare la valutazione
        } catch (error) {
            console.error("Errore nell'invio della valutazione:", error);
            toast.error("Errore nell'invio della valutazione.");
        }
    };

    const drawingStyle = {
        width: Math.max(drawingBounds.maxX - drawingBounds.minX, dimensions.width),
        height: Math.max(drawingBounds.maxY - drawingBounds.minY, dimensions.height),
    };

    return (
        <div
            style={{
                marginTop: '20px',
                marginLeft: 'auto',
                marginRight: 'auto',
                border: '1px solid #ddd',
                borderRadius: '8px',
                width: dimensions.width, // Imposta la larghezza fissa
                height: dimensions.height, // Imposta l'altezza fissa
            }}
        >
            {/* Riga superiore */}
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    padding: '10px',
                    backgroundColor: '#f1f1f1',
                    borderBottom: '1px solid #ccc',
                    borderRadius: '8px 8px 0 0',
                    width: '100%', // La larghezza è al 100% del contenitore padre
                }}
            >
                <h5 style={{ margin: 0 }}>{tema || "Nessun tema assegnato"}</h5>
                <Button
                    variant="primary"
                    className="btn-vota"
                    onClick={() => setShowValutazione(true)}
                    style={{
                        padding: '5px 10px',
                        fontSize: '14px',
                    }}
                >
                    Vota
                </Button>
            </div>

            {/* Board */}
            <div
                className="lavagna-container"
                style={{
                    overflow: 'auto',
                    width: dimensions.width,
                    height: dimensions.height,
                    position: 'relative',
                    margin: '0 auto',
                }}
            >
                <div style={{ ...drawingStyle, position: 'relative' }}>
                    <Stage
                        width={drawingStyle.width}
                        height={drawingStyle.height}
                        ref={stageRef}
                        style={{
                            backgroundColor: "transparent",
                            cursor: "default",
                        }}
                    >
                        {/* Layer 2: Immagine di sfondo */}
                        {bgImage && (
                            <Layer name="background-layer">
                                <KonvaImage
                                    image={bgImage}
                                    x={0} // Imposta le coordinate x fisse o personalizzate
                                    y={0} // Imposta le coordinate y fisse o personalizzate
                                    width={1920} // Imposta la larghezza fissa
                                    height={1080} // Imposta l'altezza fissa
                                    listening={false} // Disabilita gli eventi per il layer di sfondo
                                    onError={() => {
                                        console.error('Errore nel caricamento dell\'immagine di sfondo');
                                    }}
                                />
                            </Layer>
                        )}
                        <Layer name="drawing-layer">
                            {actions.map((action, index) => {
                                if (action.type === "lasso") {
                                    return (
                                        <Line
                                            key={`action-${index}`}
                                            points={action.points}
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
                                            points={action.points}
                                            stroke={action.color}
                                            strokeWidth={action.strokeWidth}
                                            lineCap="round"
                                            lineJoin="round"
                                            tension={0.5}
                                            globalCompositeOperation={action.color === "white" ? 'destination-out' : 'source-over'}
                                        />
                                    );
                                } else {
                                    return null;
                                }
                            })}
                        </Layer>
                    </Stage>
                </div>

                <ValutazionePopup
                    show={showValutazione}
                    onClose={() => setShowValutazione(false)}
                    onSubmit={handleSubmitValutazione}
                    valutazione={valutazione}
                    setValutazione={setValutazione}
                    commento={commento}
                    setCommento={setCommento}
                />
            </div>
        </div>
    );
};

export default MostraDisegnoBambino;
