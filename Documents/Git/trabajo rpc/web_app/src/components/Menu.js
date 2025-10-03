import React, { useState } from "react";
import Inventario from "./Inventario";
import Evento from "./Evento";
import GestionUsuarios from "./GestionUsuarios";
import "./Menu.css";

function Menu({ usuarios, onLogout }) {
  const [vista, setVista] = useState("menu");

  return (
    <div className="menu-container">
      <div className="menu-card">
        <h2 className="menu-title">
          Bienvenido, {usuarios.nombre}{" "}
          <span className="menu-role">({usuarios.rol})</span>
        </h2>

        <button className="logout-button" onClick={onLogout}>
          Cerrar sesión
        </button>

        <hr className="menu-divider" />

        <div className="menu-options">
          {usuarios.rol === "PRESIDENTE" && (
            <button onClick={() => setVista("gestionUsuarios")}>
              Gestión de Usuarios
            </button>
          )}

          {usuarios.rol === "PRESIDENTE" && (
            <button onClick={() => setVista("inventario")}>
              Inventario
            </button>
          )}

          {(usuarios.rol === "PRESIDENTE" || usuarios.rol === "VOCAL") && (
            <button onClick={() => setVista("evento")}>
              Eventos
            </button>
          )}
        </div>

        <div className="menu-content">
          {vista === "gestionUsuarios" && usuarios.rol === "PRESIDENTE" && (
            <GestionUsuarios />
          )}
          {vista === "inventario" && <Inventario />}
          {vista === "evento" && <Evento />}
          {vista === "menu" && <div>Seleccione una opción</div>}
        </div>
      </div>
    </div>
  );
}

export default Menu;
