import React, { useState, useEffect } from "react";
import { enviarOferta } from "../servicios/donacionesCliente";
import "./Donaciones.css";

const OfrecerDonacion = ({ idOrganizacion }) => {
  const [donaciones, setDonaciones] = useState([]);
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [cantidad, setCantidad] = useState("");
  const [mensaje, setMensaje] = useState("");

  useEffect(() => {
    console.log("[OfrecerDonacion] componente renderizado");
  }, []);

  const agregarItem = () => {
    if (categoria && descripcion && cantidad) {
      const nuevoItem = { categoria, descripcion, cantidad: parseInt(cantidad) };
      setDonaciones([...donaciones, nuevoItem]);
      console.log("[OfrecerDonacion] item agregado:", nuevoItem);
      setCategoria(""); setDescripcion(""); setCantidad("");
    }
  };

  const enviar = async () => {
    if (!donaciones.length) return;

    const idOferta = `OFR-${Date.now()}`;
    console.log("[OfrecerDonacion] enviando oferta:", { idOferta, idOrganizacion, donaciones });

    try {
      const resp = await enviarOferta(idOferta, idOrganizacion, donaciones);
      console.log("[OfrecerDonacion] respuesta del servidor:", resp);
      setMensaje(resp);
      setDonaciones([]);
    } catch (err) {
      console.error("[OfrecerDonacion] error al enviar oferta:", err);
      setMensaje("Error al enviar oferta");
    }
  };

  return (
    <div className="donacion-container">
      <h2>Ofrecer Donaciones</h2>
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
        <input
          type="number"
          placeholder="Cantidad"
          value={cantidad}
          onChange={e => setCantidad(e.target.value)}
        />
        <button onClick={agregarItem}>Agregar</button>
      </div>

      <ul>
        {donaciones.map((d, i) => (
          <li key={i}>{d.categoria} - {d.descripcion} ({d.cantidad})</li>
        ))}
      </ul>

      <button onClick={enviar} disabled={!donaciones.length}>Enviar Oferta</button>
      {mensaje && <p className="mensaje">{mensaje}</p>}
    </div>
  );
};

export default OfrecerDonacion;
