import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";

/**
 * @autor gabrieleristallo
 * componente utilizzata per visualizzare i dettagli di un bambino
 * grazie ad useParams si ottiene l'id del bambino passato tramite url
 */

function DettaglioBambinoComponent() {
    const { id } = useParams();
    const [bambino, setBambino] = useState(null);

    useEffect(() => {
        const bambini = [
            { id: 1, codice: 11111, nome: "Giacomo", cognome: "Rossi", sesso: "M", dataDiNascita: "21-12-2009", codiceFiscale: "AAABBB09A21D390N", emailGenitore: "genitor@gmail.com", telefonoGenitore: "1234567890" },
            { id: 2, codice: 22222, nome: "Luca", cognome: "Verdi", sesso: "M", dataDiNascita: "15-05-2010", codiceFiscale: "CCCDDD10E15G123K", emailGenitore: "genitore2@gmail.com", telefonoGenitore: "0987654321" },
            { id: 3, codice: 33333, nome: "Sofia", cognome: "Bianchi", sesso: "F", dataDiNascita: "03-09-2011", codiceFiscale: "EEFFFF11C03H456L", emailGenitore: "genitore3@gmail.com", telefonoGenitore: "1122334455" },
            { id: 4, codice: 44444, nome: "Matteo", cognome: "Neri", sesso: "M", dataDiNascita: "27-07-2008", codiceFiscale: "GGGHHH08G27J789M", emailGenitore: "genitore4@gmail.com", telefonoGenitore: "2233445566" },
            { id: 5, codice: 55555, nome: "Emma", cognome: "Rosa", sesso: "F", dataDiNascita: "10-03-2012", codiceFiscale: "IIJJJJ12H10K012N", emailGenitore: "genitore5@gmail.com", telefonoGenitore: "3344556677" },
            { id: 6, codice: 66666, nome: "Mario", cognome: "Marroni", sesso: "M", dataDiNascita: "19-07-2010", codiceFiscale: "IIJJ012DFS7S7D6N", emailGenitore: "genitore5@gmail.com", telefonoGenitore: "3344556677" }
        ];
        const bambinoTrovato = bambini.find(b => b.id === parseInt(id));
        setBambino(bambinoTrovato);
    }, [id]);

    if (!bambino) {
        return <p>Caricamento in corso...</p>;
    }

    // Stili inline
    const styles = {
        container: {
            padding: "20px",
            backgroundColor: "#B8E1FF",
            borderRadius: "8px",
            boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
            margin: "20px auto",
            maxWidth: "800px"
        },
        header: {
            color: "#f6f5e3",
            fontSize: "24px",
            fontWeight: "bold",
            textAlign: "center",
            marginBottom: "20px"
        },
        section: {
            marginBottom: "15px",
            display: "grid",
            gridTemplateColumns: "1fr 1fr",
            gap: "20px"
        },
        label: {
            fontWeight: "bold",
            color: "#2c3e50",
            marginRight: "10px"
        },
        value: {
            color: "#333"
        },
        dataRow: {
            display: "flex",
            alignItems: "center",
            marginBottom: "10px"
        },
        buttonLink: {
            textDecoration: "none",
            fontWeight: "bold"
        },
        buttonText: {
            textAlign: "center",
            marginTop: "10px",
            cursor: "pointer",
            paddingRight: "10px",
            color: "#f6f5e3",
            fontWeight: "bold",
            margin: "5px"
        },
        buttonContainer: {
            display: "flex",
            justifyContent: "space-between",
            marginTop: "20px"
        },
        deleteButton: {
            backgroundColor: "#BCB6FF",
            color: "white",
            padding: "8px 15px",
            borderRadius: "5px",
            cursor: "pointer",
            fontWeight: "bold",
            textAlign: "center"
        },
        editButton: {
            backgroundColor: "#94FBAB",
            color: "white",
            padding: "8px 15px",
            borderRadius: "5px",
            cursor: "pointer",
            fontWeight: "bold",
            textAlign: "center"
        }
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.header}>{bambino.nome} {bambino.cognome}</h2>

            <div style={styles.section}>
                <div style={styles.dataRow}>
                    <label style={styles.label}>Codice:</label>
                    <span style={styles.value}>{bambino.codice}</span>
                </div>
                <div style={styles.dataRow}>
                    <label style={styles.label}>Sesso:</label>
                    <span style={styles.value}>{bambino.sesso === 'M' ? 'Maschio' : 'Femmina'}</span>
                </div>
                <div style={styles.dataRow}>
                    <label style={styles.label}>Data di Nascita:</label>
                    <span style={styles.value}>{bambino.dataDiNascita}</span>
                </div>
                <div style={styles.dataRow}>
                    <label style={styles.label}>Codice Fiscale:</label>
                    <span style={styles.value}>{bambino.codiceFiscale}</span>
                </div>
                <div style={styles.dataRow}>
                    <label style={styles.label}>Email Genitore:</label>
                    <span style={styles.value}>{bambino.emailGenitore}</span>
                </div>
                <div style={styles.dataRow}>
                    <label style={styles.label}>Telefono Genitore:</label>
                    <span style={styles.value}>{bambino.telefonoGenitore}</span>
                </div>
            </div>

            <div style={styles.buttonContainer}>
                <Link to={'/modificaBambino'} style={styles.buttonLink}>
                    <p style={styles.editButton}>Modifica →</p>
                </Link>
                <Link to={`/eliminaBambino/${id}`} style={styles.buttonLink}>
                    <p style={styles.deleteButton}>Elimina</p>
                </Link>
            </div>

            <Link to={'/'} style={styles.buttonLink}>
                <p style={{ ...styles.buttonText, ...styles.buttonLeft }}>↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default DettaglioBambinoComponent;
