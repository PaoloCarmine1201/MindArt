import React from 'react';
import '../style/BambinoListItemStyle.css'
/**
 * @autor gabrieleristallo
 * @param props
 * componente utilizzato per la visualizzazione in una lista di un bambino
 */

function BambinoListItemComponent(props) {
    const {nome, cognome} = props.bambino;
    return (
        <div style={{color: "#2c3e50", paddingLeft: "5px", backgroundColor: "#BCB6FF", margin: "3px", verticalAlign: "middle", borderRadius: "2px"}}>
            <p style={{padding: "4px", fontWeight: "bold", cursor: "pointer"}}>{nome} {cognome}</p>
        </div>
    );
}

export default BambinoListItemComponent;