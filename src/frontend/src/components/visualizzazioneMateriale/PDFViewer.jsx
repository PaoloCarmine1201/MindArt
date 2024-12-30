import React, { useState } from "react";
import { Document, Page, pdfjs } from "react-pdf";
import "react-pdf/dist/esm/Page/AnnotationLayer.css";
import "react-pdf/dist/esm/Page/TextLayer.css";
import "./PDFViewer.css";

// Configurazione del worker locale
pdfjs.GlobalWorkerOptions.workerSrc = `${process.env.PUBLIC_URL}/pdf.worker.min.mjs`;

const PDFViewer = ({ file }) => {
    const [numPages, setNumPages] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);

    const onDocumentLoadSuccess = ({ numPages }) => {
        setNumPages(numPages);
        setCurrentPage(1);
    };

    const goToPrevPage = () => {
        if (currentPage > 1) setCurrentPage(currentPage - 1);
    };

    const goToNextPage = () => {
        if (currentPage < numPages) setCurrentPage(currentPage + 1);
    };

    return (
        <div className="pdf-viewer-container">
            <div className="navigation">
                <button onClick={goToPrevPage} disabled={currentPage === 1}>
                    ◀️ Indietro
                </button>
                <span>
          Pagina {currentPage} di {numPages || "?"}
        </span>
                <button onClick={goToNextPage} disabled={currentPage === numPages}>
                    Avanti ▶️
                </button>
            </div>
            <div className="pdf-document">
                <Document file={file} onLoadSuccess={onDocumentLoadSuccess}>
                    <Page pageNumber={currentPage} />
                </Document>
            </div>
        </div>
    );
};

export default PDFViewer;
