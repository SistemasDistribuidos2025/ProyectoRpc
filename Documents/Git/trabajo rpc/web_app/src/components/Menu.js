import React from "react";
import Usuarios from "./Usuarios";
import Inventario from "./Inventario";
import Evento from "./Evento";
import "./Menu.css";


function Menu({ usuarios, onLogout }) {

  return (
    <div className="menu-container">
      <div className="menu-card">
        <h2 className="menu-title">
          Bienvenido, {usuarios.nombre} <span className="menu-role">({usuarios.rol})</span>
        </h2>
        
        <button className="logout-button" onClick={onLogout}>
          Cerrar sesión
        </button>

        <hr className="menu-divider" />

        <div className="menu-content">
          {usuarios.rol === "PRESIDENTE" && (
            <>
              <Usuarios />
              <Inventario />
              <Evento />
            </>
          )}
          {usuarios.rol === "VOCAL" && <Inventario />}
          {usuarios.rol === "COORDINADOR" && <Evento />}
          {usuarios.rol === "VOLUNTARIO" && <Evento />}
        </div>
      </div>
    </div>
  );
}

export default Menu;
