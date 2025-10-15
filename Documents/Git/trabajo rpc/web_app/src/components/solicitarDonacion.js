import React, { useState, useEffect } from "react";
import { enviarSolicitud, darBajaSolicitud } from "../servicios/donacionesCliente";
import "./Donaciones.css";

const SolicitarDonacion = ({ idOrganizacion }) => {
  const [donaciones, setDonaciones] = useState([]);
  const [solicitudesEnviadas, setSolicitudesEnviadas] = useState([]); 
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [mensaje, setMensaje] = useState("");

  useEffect(() => {
  console.log("[SolicitarDonacion] idOrganizacion recibido:", idOrganizacion);
}, [idOrganizacion]);


  useEffect(() => {
    console.log("[SolicitarDonacion] componente renderizado");
  }, []);

  const agregarItem = () => {
    if (categoria && descripcion) {
      const nuevoItem = { categoria, descripcion };
      setDonaciones([...donaciones, nuevoItem]);
      console.log("[SolicitarDonacion] item agregado:", nuevoItem);
      setCategoria("");
      setDescripcion("");
    }
  };

  const enviar = async () => {
    if (!donaciones.length) return;

    const idSolicitud = `SOL-${Date.now()}`;
    const nuevaSolicitud = { idSolicitud, idOrganizacion, donaciones };

    console.log("[SolicitarDonacion] enviando solicitud:", nuevaSolicitud);

    try {
      const resp = await enviarSolicitud(idOrganizacion, idSolicitud, donaciones);
      console.log("[SolicitarDonacion] respuesta del servidor:", resp);
      setMensaje("✅ Solicitud enviada correctamente");
      setSolicitudesEnviadas([...solicitudesEnviadas, nuevaSolicitud]);

      setDonaciones([]);
    } catch (err) {
      console.error("[SolicitarDonacion] error al enviar solicitud:", err);
      setMensaje("❌ Error al enviar solicitud");
    }
  };

  const bajaSolicitud = async (solicitud) => {
    try {
      const resp = await darBajaSolicitud(solicitud.idOrganizacion, solicitud.idSolicitud);
      console.log("[SolicitarDonacion] Baja respuesta:", resp);

      // Actualizar lista local
      setSolicitudesEnviadas(solicitudesEnviadas.filter(s => s.idSolicitud !== solicitud.idSolicitud));
      setMensaje("✅ Solicitud dada de baja correctamente");
    } catch (err) {
      console.error("[SolicitarDonacion] Error al dar de baja:", err);
      setMensaje("❌ Error al dar de baja la solicitud");
    }
};


  return (
    <div className="donacion-container">
      <h2>Solicitar Donaciones</h2>

      <div className="input-row">
        <input
          type="text"
          placeholder="Categoría"
          value={categoria}
          onChange={e => setCategoria(e.target.value)}
        />
        <input
          type="text"
          placeholder="Descripción"
          value={descripcion}
          onChange={e => setDescripcion(e.target.value)}
        />
        <button className="donacion-btn" onClick={agregarItem}>Agregar</button>
      </div>

      <h3>Solicitudes pendientes</h3>
      {donaciones.length ? (
        <ul>
          {donaciones.map((d, i) => (
            <li className="list-solicitudes" key={i}>{d.categoria} - {d.descripcion}</li>
          ))}
        </ul>
      ) : (
        <p>No hay solicitudes cargadas.</p>
      )}

      <button className="donacion-btn" onClick={enviar} disabled={!donaciones.length}>
        Enviar Solicitud
      </button>

      {mensaje && <p className="mensaje">{mensaje}</p>}

      <h3>Solicitudes enviadas</h3>
      {solicitudesEnviadas.length ? (
        <ul>
          {solicitudesEnviadas.map((sol, i) => (
            <li className="list-solicitudes" key={i}>
              <strong>{sol.idSolicitud}</strong> — {sol.donaciones.length} ítems
              <button className="donacion-btn" onClick={() => bajaSolicitud(sol)}>
                Dar de baja
              </button>
              <ul>
                {sol.donaciones.map((d, j) => (
                  <li className="list-solicitudes" key={j}>{d.categoria} - {d.descripcion}</li>
                ))}
              </ul>
            </li>
          ))}
        </ul>

      ) : (
        <p>Todavía no se enviaron solicitudes.</p>
      )}
    </div>
  );
};

export default SolicitarDonacion;
