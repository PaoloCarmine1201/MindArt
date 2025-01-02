import React, {useEffect, useState} from 'react';
import FileViewer from '../components/visualizzazioneMateriale/FileViewer';
import background from "../assets/ChildLoginBackground.jpg";
import { Stack } from "react-bootstrap";
import '../components/visualizzazioneMateriale/MiniVideoPlayer.css';
import axiosInstance from "../config/axiosInstance"; // Assuming your CSS for MiniVideoPlayer is in this file

/**
 *
 * @param codiceBambino
 * @returns {Element}
 * @constructor
 */
const App = () => {
    const [materiale, setMateriale] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        if(materiale!= null){
            return;
        }
        const fetchMateriale = async () => {
            try {
                const response = await axiosInstance.get(`/api/bambino/sessione/getMateriale/`);
                setMateriale(response.data);
            } catch (err) {
                setError("Errore durante il caricamento del materiale.");
                console.error(err);
            }
        };

        fetchMateriale();
    });

    if (error) return <div>{error}</div>;

    if (!materiale) return <div>Caricamento...</div>;
    console.log(materiale);
    const { nome, tipoMateriale, file } = materiale;

    // Decodifica il base64 e crea un URL temporaneo
    const blob = new Blob([Uint8Array.from(atob(file), (c) => c.charCodeAt(0))], {type: "application/*"});
    const fileURL = URL.createObjectURL(blob);

    return (
        <>
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
                    zIndex: '-2',
                }}
            >
                <h1 className="app-heading">Visualizzatore di file</h1>

                <div className="mini-video-container">

                    <FileViewer
                        fileUrl={fileURL}
                        fileType={tipoMateriale}
                    />

                </div>
            </Stack>
        </>
    );
};

export default App;
