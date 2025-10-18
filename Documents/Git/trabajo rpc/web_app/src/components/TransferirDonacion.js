import React, { useState, useEffect } from "react";
import { enviarTransferencia, listarSolicitudes } from "../servicios/donacionesCliente";
import "./Donaciones.css";

const TransferirDonacion = ({ idOrganizacionDonante }) => {
  const [idSolicitud, setIdSolicitud] = useState("");
  const [idOrganizacionReceptora, setIdOrganizacionReceptora] = useState("");
  const [donaciones, setDonaciones] = useState([]);
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [cantidad, setCantidad] = useState("");
  const [mensaje, setMensaje] = useState("");
  const [solicitudesExternas, setSolicitudesExternas] = useState([]);

  useEffect(() => {
    console.log("[TransferirDonacion] componente renderizado");
  }, []);

  useEffect(() => {
      const fetchSolicitudes = async () => {
        try {
          const lista = await listarSolicitudes();
          setSolicitudesExternas(lista);
        } catch (err) {
          console.error("Error al listar solicitudes externas:", err);
        }
      };
  
      fetchSolicitudes();
      const interval = setInterval(fetchSolicitudes, 5000);
      return () => clearInterval(interval);
    }, []);
  

  const agregarItem = () => {
    if (categoria && descripcion && cantidad) {
      const nuevoItem = { categoria, descripcion, cantidad: parseInt(cantidad) };
      setDonaciones([...donaciones, nuevoItem]);
      console.log("[TransferirDonacion] item agregado:", nuevoItem);
      setCategoria(""); setDescripcion(""); setCantidad("");
    }
  };

  const enviar = async () => {
    if (!donaciones.length || !idSolicitud || !idOrganizacionReceptora) return;

    console.log("[TransferirDonacion] enviando transferencia:", {
      idSolicitud, idOrganizacionDonante, idOrganizacionReceptora, donaciones
    });
  };

  const transferirSolicitud = async (solicitud) => {
    try {

      const transferencia = {
        idSolicitud: solicitud.idSolicitud,
        idOrganizacionDonante: idOrganizacionDonante,
        idOrganizacionReceptora: solicitud.idOrganizacion,
        donaciones: solicitud.donaciones
      };

      const resp = await enviarTransferencia(
        transferencia.idSolicitud,
        transferencia.idOrganizacionDonante,
        transferencia.idOrganizacionReceptora,
        transferencia.donaciones
      );

      console.log("[TransferirDonacion] Respuesta del backend:", resp);
      setMensaje(`Transferencia enviada para la solicitud ${solicitud.idSolicitud}`);
    } catch (err) {
      console.error("[TransferirDonacion] Error al enviar transferencia:", err);
      setMensaje("Error al realizar transferencia");
    }
  };
    

  

  return (
    <div className="donacion-container">
      <h2>Transferir Donaciones</h2>
      <input
        type="text"
        placeholder="ID Solicitud"
        value={idSolicitud}
        onChange={e => setIdSolicitud(e.target.value)}
      />
      <input
        type="text"
        placeholder="ID Organización Receptora"
        value={idOrganizacionReceptora}
        onChange={e => setIdOrganizacionReceptora(e.target.value)}
      />

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
        <button className="donacion-btn" onClick={agregarItem}>Agregar</button>
      </div>

      <ul>
        {donaciones.map((d, i) => (
          <li key={i}>{d.categoria} - {d.descripcion} ({d.cantidad})</li>
        ))}
      </ul>

      <button className="donacion-btn" onClick={enviar} disabled={!donaciones.length || !idSolicitud || !idOrganizacionReceptora}>
        Enviar Transferencia
      </button>
      {mensaje && <p className="mensaje">{mensaje}</p>}


      <h3>Solicitudes</h3>
      {solicitudesExternas.length ? (
        <ul>
          {solicitudesExternas.map((sol, i) => (
            <li className="list-solicitudes" key={i}>
              <strong>{sol.idSolicitud}</strong> — {sol.donaciones.length} ítems
              <ul>
                {sol.donaciones.map((d, j) => (
                  <li className="list-solicitudes" key={j}>{d.categoria} - {d.descripcion} ({d.cantidad || "sin cantidad"})</li>
                ))}
              </ul>
              <button className="donacion-btn" onClick={() => transferirSolicitud(sol)}>
                Realizar transferencia
              </button>

            </li>
          ))}
        </ul>
      ) : <p>No hay solicitudes externas.</p>}
    </div>
  );
}


export default TransferirDonacion;
