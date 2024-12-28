// FileViewer.jsx
import React, { useState } from 'react';
import MiniVideoPlayer from './MiniVideoPlayer';
import PDFViewer from './PDFViewer';
import PPTXViewer from "./PPTXViewer";

// Esempio con lo switch case
const FileViewer = ({ fileUrl, fileType }) => {
    // Se vuoi far comparire o scomparire il viewer PDF
    const [showPdfViewer, setShowPdfViewer] = useState(true);
    // Per i video
    const [showVideoPlayer, setShowVideoPlayer] = useState(true);

    const [showPPTXPlayer, setShowPPTXPlayer] = useState(true);

    switch (fileType) {
        case 'pdf':
            return showPdfViewer ? (
                <PDFViewer
                    file={fileUrl}
                    //onClose={() => setShowPdfViewer(false)}
                />
            ) : (
                <p>PDF chiuso</p>
            );

        case 'video':
            return showVideoPlayer ? (
                <MiniVideoPlayer
                    videoUrl={fileUrl}
                    onClose={() => setShowVideoPlayer(false)}
                />
            ) : (
                <p>Video chiuso</p>
            );

        case 'pptx':
            return (
                <PPTXViewer
                    pptxUrl={fileType}
                    //onClose={setShowPPTXPlayer(false)}
                />
            );

        default:
            return <p>Formato non supportato o fileType non riconosciuto.</p>;
    }
};

export default FileViewer;
