import { useState, useEffect } from 'react';
import BambinoListItemComponent from './BambinoListItemComponent.js';
import {Link} from "react-router-dom";
import '../style/GestioneBambini.css'

/**
 *
 * @autor gabrieleristallo
 * componente utilizzata per la visualizzazione e la gestione dei bambini
 * è possibile cliccare sui bambini per visualizzarne i dettagli
 * è possibile cliccare su aggiungi bambino per aggiungerne uno
 */
function GestioneBambiniComponents() {
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
            <h2 style={{color: "#f6f5e3", fontWeight: "bold"}}>Gestione Bambini</h2>
            <div className="container">
                {
                    bambini && bambini.length > 0 ? (
                        bambini.map((bambino) => (
                            <Link to={`/dettaglioBambino/${bambino.id}`}><BambinoListItemComponent key={bambino.id} bambino={bambino} /></Link>
                        ))
                    ) : (
                        <p>Nessun bambino trovato</p>
                    )
                }
            </div>
            <Link to={'/aggiungiBambino'} style={{textDecoration: "none", fontWeight: "bold"}}><p style={{
                textAlign: "right",
                marginTop: "10px",
                cursor: "pointer",
                paddingRight: "10px",
                color: "#f6f5e3",
                fontWeight: "bold"
            }}>Aggiungi Bambino →</p>
            </Link>
            <Link to={'/'} style={{textDecoration: "none", fontWeight: "bold"}}><p style={{
                textAlign: "left",
                marginTop: "10px",
                cursor: "pointer",
                paddingRight: "10px",
                color: "#f6f5e3",
                fontWeight: "bold"
            }}>↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default GestioneBambiniComponents;
