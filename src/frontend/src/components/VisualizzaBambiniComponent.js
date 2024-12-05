import {useEffect, useState} from "react";
import BambinoListItemComponent from "./BambinoListItemComponent";
import {Link} from "react-router-dom";

/**
 * @autor gabrieleristallo
 * Componente destinata ad essere usata nella home page del terapeuta
 * consente la visualizzazione generica di un numero limitato di bambini
 * un link offre la possibilità di accedere alla pagina per la propria gestione
 */

function VisualizzaBambiniComponent() {
    const [bambini, setBambini] = useState([]);

    useEffect(() => {
        const fetchBambini = async () => {
            const datiSimulati = [
                { id: 1, codice: 11111, nome: "Giacomo", cognome: "Rossi", sesso: "M", dataDiNascita: "21-12-2009", codiceFiscale: "AAABBB09A21D390N", emailGenitore: "genitor@gmail.com", telefonoGenitore: "1234567890" },
                { id: 2, codice: 22222, nome: "Luca", cognome: "Verdi", sesso: "M", dataDiNascita: "15-05-2010", codiceFiscale: "CCCDDD10E15G123K", emailGenitore: "genitore2@gmail.com", telefonoGenitore: "0987654321" },
                { id: 3, codice: 33333, nome: "Sofia", cognome: "Bianchi", sesso: "F", dataDiNascita: "03-09-2011", codiceFiscale: "EEFFFF11C03H456L", emailGenitore: "genitore3@gmail.com", telefonoGenitore: "1122334455" },
                { id: 4, codice: 44444, nome: "Matteo", cognome: "Neri", sesso: "M", dataDiNascita: "27-07-2008", codiceFiscale: "GGGHHH08G27J789M", emailGenitore: "genitore4@gmail.com", telefonoGenitore: "2233445566" },
                { id: 5, codice: 55555, nome: "Emma", cognome: "Rosa", sesso: "F", dataDiNascita: "10-03-2012", codiceFiscale: "IIJJJJ12H10K012N", emailGenitore: "genitore5@gmail.com", telefonoGenitore: "3344556677" },
                { id: 6, codice: 66666, nome: "Mario", cognome: "Marroni", sesso: "M", dataDiNascita: "19-07-2010", codiceFiscale: "IIJJ012DFS7S7D6N", emailGenitore: "genitore5@gmail.com", telefonoGenitore: "3344556677" }
            ];
            setBambini(datiSimulati);
        };
        fetchBambini();
    }, []);

    return (
        <div style={{backgroundColor: "#B8E1FF", padding: "7px", margin: "5px", borderRadius: "5px"}}>
            <h2 style={{color: "#f6f5e3", fontWeight: "bold"}}>I tuoi pazienti</h2>
            <div className="container">
                {
                    bambini && bambini.length > 0 ? (
                        bambini.slice(0, 3).map((bambino) => (
                            <BambinoListItemComponent key={bambino.id} bambino={bambino} />
                        ))
                    ) : (
                        <p>Nessun bambino trovato</p>
                    )
                }
            </div>
            <Link to={'/gestioneBambini'} style={{textDecoration: "none", fontWeight: "bold"}}><p style={{
                textAlign: "right",
                textDecoration: "none",
                marginTop: "10px",
                cursor: "pointer",
                paddingRight: "10px",
                color: "#2c3e50"
            }}>Vedi tutto →</p></Link>
        </div>
    );
}

export default VisualizzaBambiniComponent;