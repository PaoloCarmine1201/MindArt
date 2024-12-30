import DettaglioBambinoComponent from "../components/VisualizzazioneBambino/DettaglioBambinoComponent";
import NavBar from "../components/HomePageTerapeuta/NavBar";

function DettagliBambino() {
    return (
        <>
            <NavBar name="Dettagli Bambino"/>
            <DettaglioBambinoComponent/>
        </>
    )
}

export default DettagliBambino;