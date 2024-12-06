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
        const fetchData = async () => {
            console.log(id);
            const result = await fetch('http://localhost:8080/api/bambino/get/' + id);
            console.log(result);
            const data = await result.json();
            console.log(data);
            setBambino(data);
        };
        fetchData();
    }, []);

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
                    <span style={styles.value}>
                      {bambino.sesso === null ? 'Sesso non disponibile' : bambino.sesso === 'M' ? 'Maschio' : 'Femmina'}
                    </span>

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

            <Link to={'/gestioneBambini'} style={styles.buttonLink}>
                <p style={{ ...styles.buttonText, ...styles.buttonLeft }}>↩︎ Indietro</p>
            </Link>
        </div>
    );
}

export default DettaglioBambinoComponent;
