import React, { useState, useEffect } from "react";
import {
  enviarTransferencia,
  listarSolicitudes,
} from "../servicios/donacionesCliente";
import "./Donaciones.css";

const TransferirDonacion = ({ idOrganizacionDonante }) => {
  const [donaciones, setDonaciones] = useState([]);
  const [categoria, setCategoria] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [cantidad, setCantidad] = useState("");
  const [mensaje, setMensaje] = useState("");
  const [solicitudesExternas, setSolicitudesExternas] = useState([]);
  const [idSolicitudSeleccionada, setIdSolicitudSeleccionada] = useState("");
  const [idOrgReceptoraSeleccionada, setIdOrgReceptoraSeleccionada] =
    useState("");

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
    if (categoria && descripcion && cantidad !== "") {
      const nuevoItem = {
        categoria,
        descripcion,
        cantidad: parseInt(cantidad),
      };
      setDonaciones([...donaciones, nuevoItem]);
      setCategoria("");
      setDescripcion("");
      setCantidad("");
    } else {
      setMensaje(
        "Debe completar categoría, descripción y cantidad para agregar un ítem."
      );
    }
  };

  const seleccionarSolicitud = (solicitud) => {
    const itemsConCantidad = solicitud.donaciones.map((d) => ({
      categoria: d.categoria,
      descripcion: d.descripcion,
      cantidad: d.cantidad !== undefined ? d.cantidad : "",
    }));

    setDonaciones(itemsConCantidad);
    setIdSolicitudSeleccionada(solicitud.idSolicitud);
    setIdOrgReceptoraSeleccionada(solicitud.idOrganizacion);
    setMensaje("");
  };

  const enviarTransferenciaClick = async () => {
    if (!donaciones.length) {
      setMensaje("Agrega al menos un ítem con cantidad antes de transferir.");
      return;
    }
    if (!idSolicitudSeleccionada || !idOrgReceptoraSeleccionada) {
      setMensaje("Selecciona una solicitud antes de transferir.");
      return;
    }

    try {
      const resp = await enviarTransferencia(
        idSolicitudSeleccionada,
        idOrganizacionDonante,
        idOrgReceptoraSeleccionada,
        donaciones
      );

      console.log("[TransferirDonacion] Respuesta del backend:", resp);
      setMensaje(
        `Transferencia enviada para la solicitud ${idSolicitudSeleccionada}`
      );
      setDonaciones([]);
      setIdSolicitudSeleccionada("");
      setIdOrgReceptoraSeleccionada("");
    } catch (err) {
      console.error("[TransferirDonacion] Error al enviar transferencia:", err);
      setMensaje("Error al realizar transferencia");
    }
  };

  return (
    <div className="donacion-container">
      <h2>Transferir Donaciones</h2>

      <div className="input-row">
        <input
          type="text"
          placeholder="Categoría"
          value={categoria}
          onChange={(e) => setCategoria(e.target.value)}
        />
        <input
          type="text"
          placeholder="Descripción"
          value={descripcion}
          onChange={(e) => setDescripcion(e.target.value)}
        />
        <input
          type="number"
          placeholder="Cantidad"
          value={cantidad}
          onChange={(e) => setCantidad(e.target.value)}
        />
        <button className="donacion-btn" onClick={agregarItem}>
          Agregar
        </button>
      </div>

      {donaciones.length > 0 && (
        <>
          <h3>Ítems a transferir</h3>
          <ul>
            {donaciones.map((d, i) => (
              <li key={i}>
                {d.categoria} - {d.descripcion} (
                {d.cantidad !== "" ? d.cantidad : "sin cantidad"})
              </li>
            ))}
          </ul>
        </>
      )}

      <button
        className="donacion-btn"
        onClick={enviarTransferenciaClick}
        disabled={
          !donaciones.length ||
          !idSolicitudSeleccionada ||
          !idOrgReceptoraSeleccionada
        }
      >
        Enviar Transferencia
      </button>

      <h3>Solicitudes Externas</h3>
      {solicitudesExternas.length ? (
        <ul>
          {solicitudesExternas.map((sol, i) => {
            const esPropia = sol.idOrganizacion === "1";
            return (
              <li
                className={`list-solicitudes ${
                  esPropia ? "propia" : "externa"
                }`}
                key={i}
                style={{
                  backgroundColor: esPropia ? "#d0f0c0" : "white",
                  border: esPropia ? "2px solid green" : "1px solid #ccc",
                  padding: "8px",
                  marginBottom: "8px",
                }}
              >
                <strong>
                  {sol.idSolicitud}
                  {esPropia && " (Propia)"}
                </strong>
                {" — "}
                {sol.donaciones.length} ítems
                <ul>
                  {sol.donaciones.map((d, j) => (
                    <li key={j}>
                      {d.categoria} - {d.descripcion} (
                      {d.cantidad || "sin cantidad"})
                    </li>
                  ))}
                </ul>
                {!esPropia && (
                  <button
                    className="donacion-btn"
                    onClick={() => seleccionarSolicitud(sol)}
                  >
                    Seleccionar para transferir
                  </button>
                )}
              </li>
            );
          })}
        </ul>
      ) : (
        <p>No hay solicitudes externas.</p>
      )}

      {mensaje && <p className="mensaje">{mensaje}</p>}
    </div>
  );
};

export default TransferirDonacion;
