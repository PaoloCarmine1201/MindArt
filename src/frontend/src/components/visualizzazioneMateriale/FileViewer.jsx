// FileViewer.jsx
import React, { useState } from 'react';
import MiniVideoPlayer from './MiniVideoPlayer';
import PDFViewer from './PDFViewer';

// Esempio con lo switch case
const FileViewer = ({ fileUrl, fileType }) => {
    // Se vuoi far comparire o scomparire il viewer PDF
    const [showPdfViewer, setShowPdfViewer] = useState(true);
    // Per i video
    const [showVideoPlayer, setShowVideoPlayer] = useState(true);

    switch (fileType) {
        case 'PDF':
            return showPdfViewer ? (
                <PDFViewer
                    file={fileUrl}
                    onClose={() => setShowPdfViewer(false)}
                />
            ) : (
                <p>PDF chiuso</p>
            );

        case 'VIDEO':
            return showVideoPlayer ? (
                <MiniVideoPlayer
                    videoUrl={fileUrl}
                    onClose={() => setShowVideoPlayer(false)}
                />
            ) : (
                <p>Video chiuso</p>
            );


        default:
            return <p>Formato non supportato o fileType non riconosciuto.</p>;
    }
};

export default FileViewer;
