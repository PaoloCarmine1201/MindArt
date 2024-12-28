import React from 'react';
import FileViewer from '../components/visualizzazioneMateriale/FileViewer';

const App = () => {
    return (
        //ADD NAVBAR/FOOTER

        <>

            <div>
                <h1>Visualizzatore di file</h1>

                {/* Esempio PDF*/}
                <FileViewer
                    fileUrl="/materiali/samplepptx.pptx" //"/materiali/aiayn.pdf"
                    fileType="pdf"
                />



                {/* Esempio Video MP4
                <FileViewer
                    fileUrl="https://samplelib.com/lib/preview/mp4/sample-5s.mp4"
                    fileType="video"
                />
                */}

                {/* Esempio PPTX
                <FileViewer
                    fileUrl= "https://scholar.harvard.edu/files/torman_personal/files/samplepptx.pptx"// "/materiali/samplepptx.pptx"
                    fileType="pptx"
                />
                */}

            </div>
        </>
    );
};

export default App;