import React, { useEffect, useState } from "react";
import { listarInventario } from "../servicios/grpcCliente";
import "./Inventario.css"; 

const Inventario = () => {
  const [inventario, setInventario] = useState([]);

  useEffect(() => {
    const getListaInventario = async () => {
      try {
        const lista = await listarInventario();
        console.log("Lista de Inventario desde gRPC:", lista);
        setInventario(lista);
      } catch (err) {
        console.error("Error cargando Inventario:", err);
      }
    };

    getListaInventario();
  }, []);

  return (
    <div className="inventario-card">
      <h2 className="section-title">📦 Lista de Inventario</h2>
      <ul className="inventario-list">
        {inventario.map((i) => (
          <li key={i.id} className="inventario-item">
            <div className="inventario-header">
              <strong>{i.nombre}</strong>
              <span className="inventario-cantidad">Cantidad: {i.cantidad}</span>
            </div>
            <p className="inventario-desc">{i.descripcion}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Inventario;
