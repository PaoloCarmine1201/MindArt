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
    };
    return mimeTypes[extension] || 'image/png';
};

const MostraDisegnoBambino = ({ disegnoId, tema }) => {
    const [actions, setActions] = useState([]);
    const stageRef = useRef(null);
    const [showValutazione, setShowValutazione] = useState(false);
    const [valutazione, setValutazione] = useState("");
    const [commento, setCommento] = useState("");

    const EMPTY_IMAGE_BASE64 = "data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=";

    const [dimensions] = useState({
        width: 1920,
        height: 1080,
    });

    const [scaleFactor, setScaleFactor] = useState(1);
    const [backgroundImage, setBackgroundImage] = useState(null);
    const [bgImage] = useImage(backgroundImage);

    useEffect(() => {
        const updateScaleFactor = () => {
            const maxWidth = window.innerWidth - 40;
            const maxHeight = window.innerHeight - 100;
            const widthScale = maxWidth / dimensions.width;
            const heightScale = maxHeight / dimensions.height;
            setScaleFactor(Math.min(widthScale, heightScale, 1));
        };

        updateScaleFactor();
        window.addEventListener("resize", updateScaleFactor);
        return () => window.removeEventListener("resize", updateScaleFactor);
    }, [dimensions]);

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
            } catch (error) {
                console.error('Errore nel caricamento del disegno:', error);
            }
        };

        const loadBackground = async () => {
            let imageUrl;
            try {
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
                    imageUrl = `data:${mimeType};base64,${base64Image}`;
                    console.log('URL dell\'immagine di sfondo:', imageUrl);
                }
                else{
                    imageUrl = EMPTY_IMAGE_BASE64;
                }
                setBackgroundImage(imageUrl);
            } catch (error) {
                console.error('Errore nel caricamento dell\'immagine di sfondo:', error);
                imageUrl = EMPTY_IMAGE_BASE64;

                setBackgroundImage(imageUrl);
            }
        }

        if (disegnoId) {
            loadActions();
            loadBackground();
        }
    }, [disegnoId]);

    const handleWheel = (e) => {
        e.evt.preventDefault();
        const stage = stageRef.current;
        const scaleBy = 1.1;
        const oldScale = stage.scaleX();
        const pointer = stage.getPointerPosition();

        const mousePointTo = {
            x: (pointer.x - stage.x()) / oldScale,
            y: (pointer.y - stage.y()) / oldScale,
        };

        const newScale = e.evt.deltaY > 0 ? oldScale / scaleBy : oldScale * scaleBy;
        stage.scale({ x: newScale, y: newScale });

        const newPos = {
            x: pointer.x - mousePointTo.x * newScale,
            y: pointer.y - mousePointTo.y * newScale,
        };
        stage.position(newPos);
        stage.batchDraw();
    };

    const handleSubmitValutazione = async () => {
        try {
            await axiosInstance.post(`/api/terapeuta/disegno/${disegnoId}/valutazione`, {
                valutazione,
            });

            toast.success("Valutazione inviata con successo!");
            setShowValutazione(false);
            window.location.reload();
        } catch (error) {
            console.error("Errore nell'invio della valutazione:", error);
            toast.error("Errore nell'invio della valutazione.");
        }
    };

    return (
        <div
            style={{
                marginTop: '20px',
                marginLeft: 'auto',
                marginRight: 'auto',
                border: '1px solid #ddd',
                borderRadius: '8px',
                overflow: 'auto',
                maxWidth: '100%',
                maxHeight: '100vh',
                width: dimensions.width * scaleFactor,
                height: dimensions.height * scaleFactor,
            }}
        >
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    padding: '10px',
                    backgroundColor: '#f1f1f1',
                    borderBottom: '1px solid #ccc',
                    borderRadius: '8px 8px 0 0',
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

            <div
                className="lavagna-container"
                style={{
                    overflow: 'auto',
                    position: 'relative',
                    margin: '0 auto',
                }}
            >
                <Stage
                    width={dimensions.width * scaleFactor}
                    height={dimensions.height * scaleFactor}
                    scaleX={scaleFactor}
                    scaleY={scaleFactor}
                    ref={stageRef}
                    onWheel={handleWheel}
                    style={{
                        backgroundColor: "transparent",
                        cursor: "default",
                    }}
                >
                    {bgImage && (
                        <Layer name="background-layer">
                            <KonvaImage
                                image={bgImage}
                                x={0}
                                y={0}
                                width={dimensions.width}
                                height={dimensions.height}
                                listening={false}
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
