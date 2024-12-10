import { Link } from "react-router-dom";
import BambinoListItemComponent from "./BambinoListItemComponent";
import '../../style/BambiniListStyle.css';
import BambinoListItemButtonComponent from "./BambinoListItemButtonComponent";

function BambiniListComponent(props) {
    console.log(props);
    const bambini = props.bambini;
    const button = props.button;
    const from = props.from;
    if(button) {
        console.log("BUTTON IS TRUEEEEE!!!!!!");
    }
    return (
        <>
            <div className="bambini-list-container">
                <div className="container">
                    {bambini && bambini.length > 0 ? (
                        bambini.map((bambino) => (
                            <Link
                                key={bambino.id}
                                to={`/dettaglioBambino/${bambino.id}`}
                                className="bambino-link"
                            >
                                {button ? <BambinoListItemButtonComponent bambino={bambino}/> :
                                    <BambinoListItemComponent bambino={bambino} />}
                                <hr className={"separatore"}></hr>
                            </Link>
                        ))
                    ) : (
                        <p>Nessun bambino trovato</p>
                    )}
                </div>
            </div>
        </>
    );
}

export default BambiniListComponent;
