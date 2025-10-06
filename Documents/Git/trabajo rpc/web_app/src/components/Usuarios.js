import React, { useEffect, useState } from "react";
import { listarUsuarios } from "../servicios/grpcCliente";
import "./Usuarios.css";

const Usuario = () => {
  const [usuario, setUsuario] = useState([]);

  useEffect(() => {
    const getListaUsuarios = async () => {
      try {
        const lista = await listarUsuarios();
        setUsuario(lista);
      } catch (err) {
        console.error("Error cargando usuarios:", err);
      }
    };

    getListaUsuarios();
  }, []);

  return (
    <div className="usuario-card">
      <h2 className="section-title">👥 Lista de Usuarios</h2>
      <ul className="usuario-list">
        {usuario.map((u) => (
          <li key={u.id} className="usuario-item">
            <span className="usuario-nombre">
              {u.nombre} {u.apellido}
            </span>
            <span className="usuario-rol">({u.rol})</span>
            <div className="usuario-email">{u.email}</div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Usuario;
