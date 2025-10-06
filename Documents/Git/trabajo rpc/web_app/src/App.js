import React, { useState } from "react";
import Login from "./components/Login";
import Menu from "./components/Menu";
import Inventario from "./components/Inventario";

function App() {
  const [usuarioLogueado, setUsuarioLogueado] = useState(null);

  return (
    <div>
      {!usuarioLogueado ? (
        <Login onLoginSuccess={setUsuarioLogueado} />
      ) : (
        <>
          {/* Menu recibe usuarioLogueado y onLogout */}
          <Menu usuarioLogueado={usuarioLogueado} onLogout={() => setUsuarioLogueado(null)} />
        </>
      )}
    </div>
  );
}

export default App;
