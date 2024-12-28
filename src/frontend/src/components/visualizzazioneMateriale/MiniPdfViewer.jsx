// MiniPdfViewer.jsx
import React, { useState } from 'react';
import { Document, Page, pdfjs } from 'react-pdf';
import './MiniPdfViewer.css';

// Imposta il worker di PDF.js al percorso locale
pdfjs.GlobalWorkerOptions.workerSrc = '/pdf.worker.min.js';

const MiniPdfViewer = ({ pdfUrl, onClose }) => {
    const [numPages, setNumPages] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);

    const onDocumentLoadSuccess = ({ numPages }) => {
        setNumPages(numPages);
        setPageNumber(1);
    };

    const goToPrevPage = () => {
        setPageNumber(prev => Math.max(prev - 1, 1));
    };

    const goToNextPage = () => {
        setPageNumber(prev => Math.min(prev + 1, numPages));
    };

    return (
        <div className="mini-pdf-viewer">
            {/* Header con bottone di chiusura */}
            <div className="pdf-viewer-header">
                <button className="close-btn" onClick={onClose}>
                    ✕
                </button>
            </div>

            {/* Contenitore documento */}
            <div className="pdf-document-wrapper">
                <Document
                    file={pdfUrl}
                    onLoadSuccess={onDocumentLoadSuccess}
                    loading={<p>Caricamento in corso...</p>}
                    noData={<p>File PDF non trovato.</p>}
                >
                    <Page
                        pageNumber={pageNumber}
                        renderTextLayer={false}
                        renderAnnotationLayer={false}
                    />
                </Document>
            </div>

            {/* Barra di navigazione pagine */}
            <div className="pdf-navigation">
                <button
                    className="nav-btn prev-btn"
                    onClick={goToPrevPage}
                    disabled={pageNumber <= 1}
                >
                    ◀︎ Indietro
                </button>

                <div className="page-info">
                    Pagina {pageNumber} di {numPages || '?'}
                </div>

                <button
                    className="nav-btn next-btn"
                    onClick={goToNextPage}
                    disabled={pageNumber >= (numPages || 0)}
                >
                    Avanti ▶︎
                </button>
            </div>
        </div>
    );
};

export default MiniPdfViewer;
