// App.jsx
import React, { useEffect, useState } from 'react';
import FileViewer from '../components/visualizzazioneMateriale/FileViewer';
import background from "../assets/ChildLoginBackground.jpg";
import { Stack } from "react-bootstrap";
import '../components/visualizzazioneMateriale/MiniVideoPlayer.css';
import axiosInstance from "../config/axiosInstance";

/**
 * Componente per visualizzare il materiale del bambino.
 */
const Materiale = () => {
    const [materiale, setMateriale] = useState(null);
    const [fileURL, setFileURL] = useState(null);
    const [fileType, setFileType] = useState(null);
    // Effetto per recuperare il materiale al montaggio del componente
    useEffect(() => {
        const fetchMateriale = async () => {
            try {
                const response = await axiosInstance.get(`/api/bambino/sessione/getMateriale/`);
                setMateriale(response.data);
            } catch (err) {
                console.error(err);
                localStorage.removeItem('jwtToken');
            }
        };

        fetchMateriale();
    }, []); // Array di dipendenze vuoto per eseguire solo una volta

    // Effetto per creare l'URL del file e determinare il tipo
    useEffect(() => {
        if (materiale && materiale.file && materiale.tipoMateriale) {
            try {
                const mimeType = materiale.tipoMateriale === 'PDF' ? 'application/pdf' : 'video/mp4';
                const dataUrl = `data:${mimeType};base64,${materiale.file}`;
                setFileURL(dataUrl);
                setFileType(materiale.tipoMateriale);
            } catch (error) {
                console.error("Errore nella decodifica del file:", error);
                localStorage.removeItem('jwtToken');
            }
        }
    }, [materiale]); // Dipende da 'materiale'


    if (!materiale || !fileURL || !fileType) return <div>Caricamento...</div>;

    return (
        <Stack
            className="d-flex justify-content-center align-items-center vh-100"
            style={{
                position: 'fixed',
                top: 0,
                left: 0,
                width: '100%',
                height: '100%',
                backgroundImage: `url(${background})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundAttachment: 'fixed',
                zIndex: 1, // Imposta un valore positivo per assicurarti che sia visibile
            }}
        >

            <div className="content-wrapper" style={{ textAlign: 'center', color: '#fff' }}>
                <h1 className="app-heading">Visualizzatore di file</h1>

                <div className="mini-video-container">
                    <FileViewer
                        fileUrl={fileURL}
                        fileType={fileType}
                    />
                </div>
            </div>
        </Stack>
    );
};

export default Materiale;
