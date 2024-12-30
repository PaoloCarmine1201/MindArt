import React from 'react';

const PPTXViewer = ({ pptxUrl, onClose }) => {
    const embedUrl = `https://view.officeapps.live.com/op/embed.aspx?src=${encodeURIComponent(
        pptxUrl
    )}`;

    return (
        <div className="pptx-viewer">
            <div className="viewer-header">
                <button className="close-btn" onClick={onClose}>
                    ×
                </button>
            </div>
            <iframe
                src={embedUrl}
                width="100%"
                height="500px"
                frameBorder="0"
                title="PPTX Viewer"
            ></iframe>
        </div>
    );
};

export default PPTXViewer;
