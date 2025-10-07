import React, { useState } from "react";
import Inventario from "./Inventario";
import Evento from "./Evento";
import GestionUsuarios from "./GestionUsuarios";
import "./Menu.css";

function Menu({ usuarioLogueado, onLogout }) {
  const [vista, setVista] = useState("menu");

  // Funciones de permiso
  const puedeGestionUsuarios = usuarioLogueado.rol === "PRESIDENTE";
  const puedeInventario = usuarioLogueado.rol === "PRESIDENTE" || usuarioLogueado.rol === "VOCAL";
  const puedeEventos = ["PRESIDENTE", "COORDINADOR", "VOLUNTARIO"].includes(usuarioLogueado.rol);

  return (
    <div className="menu-container">
      <div className="menu-card">
        <h2 className="menu-title">
          Bienvenido, {usuarioLogueado.nombre}{" "}
          <span className="menu-role">({usuarioLogueado.rol})</span>
        </h2>

        <button className="logout-button" onClick={onLogout}>
          Cerrar sesión
        </button>

        <hr className="menu-divider" />

        <div className="menu-options">
          {puedeGestionUsuarios && (
            <button onClick={() => setVista("gestionUsuarios")}>
              Gestión de Usuarios
            </button>
          )}

          {puedeInventario && (
            <button onClick={() => setVista("inventario")}>
              Inventario
            </button>
          )}

          {puedeEventos && (
            <button onClick={() => setVista("evento")}>
              Eventos
            </button>
          )}
        </div>

        <div className="menu-content">
          {vista === "gestionUsuarios" && puedeGestionUsuarios && <GestionUsuarios />}
          {vista === "inventario" && puedeInventario && <Inventario usuarioLogueado={usuarioLogueado} />}
          {vista === "evento" && puedeEventos && <Evento usuarioLogueado={usuarioLogueado} />}
          {vista === "menu" && <div>Seleccione una opción</div>}
        </div>
      </div>
    </div>
  );
}

export default Menu;
