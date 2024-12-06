import './App.css';
import React, {useState} from 'react';
import VisualizzaBambiniComponent from './components/VisualizzaBambiniComponent'

function App() {
    const [idTerapeuta, setIdTerapeuta] = useState(1); //id del terapeuta loggato DA MODIFICARE
  return (
      <>
          /*HOME DEL TERAPEUTA (CI SARANNO I VARI COMPONENTS)*/
          <VisualizzaBambiniComponent idTerapeuta={1}/>
      </>
  );
}

export default App;
