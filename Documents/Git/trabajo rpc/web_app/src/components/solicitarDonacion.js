import React, { useState, useEffect } from "react";
import { enviarSolicitud } from "../servicios/donacionesCliente";
import "./Donaciones.css";

const SolicitarDonacion = ({ idOrganizacion }) => {
  const [donaciones, setDonaciones] = useState([]);
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [mensaje, setMensaje] = useState("");

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
    console.log("[SolicitarDonacion] enviando solicitud:", { idOrganizacion, idSolicitud, donaciones });

    try {
      const resp = await enviarSolicitud(idOrganizacion, idSolicitud, donaciones);
      console.log("[SolicitarDonacion] respuesta del servidor:", resp);
      setMensaje(resp);
      setDonaciones([]);
    } catch (err) {
      console.error("[SolicitarDonacion] error al enviar solicitud:", err);
      setMensaje("Error al enviar solicitud");
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
        <button onClick={agregarItem}>Agregar</button>
      </div>

      <ul>
        {donaciones.map((d, i) => (
          <li key={i}>{d.categoria} - {d.descripcion}</li>
        ))}
      </ul>

      <button onClick={enviar}>Enviar Solicitud</button>
      {mensaje && <p className="mensaje">{mensaje}</p>}
    </div>
  );
};

export default SolicitarDonacion;
