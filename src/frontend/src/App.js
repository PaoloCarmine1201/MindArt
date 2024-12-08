import './App.css';
import React, {useEffect, useState} from 'react';
import VisualizzaBambiniComponent from './components/VisualizzaBambiniComponent'
import RegisterBambino from "./components/RegisterBambino/RegisterBambino";

function App() {
    const [idTerapeuta, setIdTerapeuta] = useState(1); //id del terapeuta loggato DA MODIFICARE
    localStorage.setItem("idTerapeuta", idTerapeuta);
    const [bambini, setBambini] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const result = await fetch('http://localhost:8080/api/bambino/getallbyterapeuta?terapeuta=' + idTerapeuta);
            console.log(result);
            const data = await result.json();
            console.log(data);
            setBambini(data);
        };
        fetchData();
    }, []);

  return (
      <>
          /*HOME DEL TERAPEUTA (CI SARANNO I VARI COMPONENTS)*/
          <VisualizzaBambiniComponent bambini={bambini}/>
          <RegisterBambino/>
      </>
  );
}

export default App;
