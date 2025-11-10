import React, { useState, useEffect } from "react";
import Login from "./components/Login";
import Menu from "./components/Menu";

function App() {
  const [usuarioLogueado, setUsuarioLogueado] = useState(null);

  useEffect(() => {
    const usuarioGuardado = localStorage.getItem("usuarioLogueado");
    if (usuarioGuardado) {
      setUsuarioLogueado(JSON.parse(usuarioGuardado));
    }
  }, []);

  const handleLoginSuccess = (usuario) => {
    setUsuarioLogueado(usuario);
  };

  const handleLogout = () => {
    localStorage.removeItem("usuarioLogueado");
    setUsuarioLogueado(null);
  };

  return (
    <div>
      {usuarioLogueado ? (
        <Menu usuarioLogueado={usuarioLogueado} onLogout={handleLogout} />
      ) : (
        <Login onLoginSuccess={handleLoginSuccess} />
      )}
    </div>
  );
}

export default App;

