import './App.css';
import { Link } from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
      <div>
          <h3>Pagine sviluppate fino ad ora</h3>
          <ul>
              <li><Link to="/login">Login</Link></li>
              <li><Link to="/register">Registration</Link></li>
              <li><Link to="/childlogin">Child Login</Link></li>
              <li><Link to="/home">Home Page</Link></li>
              <li><Link to="/calendarioEventi">Eventi terapeuta</Link></li>
              <li><Link to="/gestioneBambini">Gestione bambini</Link></li>
              <li><Link to="/gestioneMateriale">Gestione materiale</Link></li>
              <li><Link to="/visualizzazioneMateriale">Visualizzazione Materiale</Link></li>
          </ul>
      </div>
  );
}

export default App;
