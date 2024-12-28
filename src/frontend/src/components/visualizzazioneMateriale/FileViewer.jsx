// FileViewer.jsx
import React, { useState } from 'react';
import MiniVideoPlayer from './MiniVideoPlayer';
import MiniPdfViewer from './MiniPdfViewer';

// Esempio con lo switch case
const FileViewer = ({ fileUrl, fileType }) => {
    // Se vuoi far comparire o scomparire il viewer PDF
    const [showPdfViewer, setShowPdfViewer] = useState(true);
    // Per i video
    const [showVideoPlayer, setShowVideoPlayer] = useState(true);

    switch (fileType) {
        case 'pdf':
            return showPdfViewer ? (
                <MiniPdfViewer
                    pdfUrl={fileUrl}
                    onClose={() => setShowPdfViewer(false)}
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
            const pptxViewerUrl = `https://view.officeapps.live.com/op/view.aspx?src=${encodeURIComponent(
                fileUrl
            )}`;

            return (
                <iframe
                    src={pptxViewerUrl}
                    width="100%"
                    height="600px"
                    style={{ border: 'none' }}
                    title="PPTX Viewer"
                />
            );

        default:
            return <p>Formato non supportato o fileType non riconosciuto.</p>;
    }
};

export default FileViewer;
