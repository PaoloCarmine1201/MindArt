import GestioneBambiniComponent from "../components/VisualizzazioneBambino/GestioneBambiniComponent";
import NavBar from "../components/HomePageTerapeuta/NavBar";

function GestioneBambini() {
    return (
        <>
            <NavBar name={"Pazienti"}/>
            <div style={{marginTop: "10px"}}>
                <GestioneBambiniComponent/>
            </div>
        </>
    );
}

export default GestioneBambini;