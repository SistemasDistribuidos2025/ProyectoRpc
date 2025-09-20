import React, { useState } from "react";
import Login from "./components/Login";
import Menu from "./components/Menu";

function App() {
  const [usuarios, setUsuarios] = useState(null);

  return (
    <div>
      {usuarios ? (
        <Menu usuarios={usuarios} onLogout={() => setUsuarios(null)} />
      ) : (
        <Login onLoginSuccess={setUsuarios} />
      )}
    </div>
  );
}

export default App;
