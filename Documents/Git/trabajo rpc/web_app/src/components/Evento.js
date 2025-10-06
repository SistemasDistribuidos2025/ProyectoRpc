import React, { useEffect, useState } from "react";
import { listarEventos} from "../servicios/grpcCliente";
import "./Evento.css";

const Evento = () => {
  const [evento, setEvento] = useState([]);

  useEffect(() => {
    const getListaEventos = async () => {
      try {
        const lista = await listarEventos();
        console.log("Lista de eventos desde gRPC:", lista);
        setEvento(lista);
      } catch (err) {
        console.error("Error cargando Eventos:", err);
      }
    };

    getListaEventos();
  }, []);

  return (
    <div className="evento-card">
      <h2 className="section-title">📅 Lista de Eventos</h2>
      <ul className="evento-list">
        {evento.map((e) => (
          <li key={e.id} className="evento-item">
            <div className="evento-header">
              <strong>{e.nombreevento}</strong>
              <span className="evento-date">{e.fechahoraevento}</span>
            </div>
            <p className="evento-desc">{e.descripcion}</p>

            {e.participanteseventoList?.length > 0 && (
              <ul className="participantes-list">
                {e.participanteseventoList.map((p) => (
                  <li key={p.id} className="participante-item">
                    👤 {p.nombre} {p.apellido}
                  </li>
                ))}
              </ul>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Evento;
