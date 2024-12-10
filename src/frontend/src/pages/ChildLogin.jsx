import React from 'react';
import '../style/ChildLogin.css';

function ChildLogin() {
    const [inputValue, setInputValue] = React.useState("");

    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    }

    const handleButtonClick = () => {
        console.log("Entrando con codice: " + inputValue);
    }

    return (
        <div className={"child-login-container"}>
            <h1>Partecipa alla sessione!</h1>
            <input
                type={"text"}
                placeholder={"Inserisci il tuo codice..."}
                value = {inputValue}
                onChange = {handleInputChange}
            />
            <button onClick={handleButtonClick}> Entra </button>
        </div>
    );
}

export default ChildLogin;