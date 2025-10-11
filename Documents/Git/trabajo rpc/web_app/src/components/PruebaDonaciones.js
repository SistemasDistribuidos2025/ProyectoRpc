import React, { useEffect, useState } from "react";
import {
  enviarSolicitud,
  listarSolicitudes,
  enviarOferta,
  listarOfertas,
  enviarTransferencia
} from "../servicios/donacionesCliente";

export default function PruebaDonaciones() {
  const [solicitudes, setSolicitudes] = useState([]);
  const [ofertas, setOfertas] = useState([]);
  const [transferencias, setTransferencias] = useState([]);

  // ----------- Cargar listas al inicio -----------
  useEffect(() => {
    actualizarSolicitudes();
    actualizarOfertas();
  }, []);

  const actualizarSolicitudes = () => {
    listarSolicitudes()
      .then(lista => setSolicitudes(lista))
      .catch(console.error);
  };

  const actualizarOfertas = () => {
    listarOfertas()
      .then(lista => setOfertas(lista))
      .catch(console.error);
  };

  // ----------- Funciones de prueba -----------
  const handleEnviarSolicitud = () => {
    enviarSolicitud(1, Math.floor(Math.random() * 1000), [
      { categoria: "Alimentos", descripcion: "Arroz", cantidad: 10 },
      { categoria: "Ropa", descripcion: "Camisas", cantidad: 5 }
    ])
      .then(mensaje => {
        console.log("Solicitud enviada:", mensaje);
        actualizarSolicitudes();
      })
      .catch(console.error);
  };

  const handleEnviarOferta = () => {
    enviarOferta(Math.floor(Math.random() * 1000), 2, [
      { categoria: "Medicamentos", descripcion: "Paracetamol", cantidad: 20 },
      { categoria: "Agua", descripcion: "Botellas", cantidad: 50 }
    ])
      .then(mensaje => {
        console.log("Oferta enviada:", mensaje);
        actualizarOfertas();
      })
      .catch(console.error);
  };

  const handleEnviarTransferencia = () => {
    if (!solicitudes.length) {
      alert("Primero hay que tener solicitudes");
      return;
    }

    const solicitud = solicitudes[0]; // Tomamos la primera solicitud como ejemplo

    enviarTransferencia(
      solicitud.idSolicitud,
      solicitud.idOrganizacion,
      99, // Id de organización receptora de ejemplo
      solicitud.donaciones
    )
      .then(mensaje => {
        console.log("Transferencia enviada:", mensaje);
        setTransferencias(prev => [...prev, { ...solicitud }]);
      })
      .catch(console.error);
  };

  // ----------- Renderizado -----------
  return (
    <div style={{ padding: "20px" }}>
      <h2>Prueba de Donaciones / Ofertas / Transferencias</h2>

      <div style={{ marginBottom: "20px" }}>
        <button onClick={handleEnviarSolicitud}>Enviar Solicitud</button>
        <button onClick={handleEnviarOferta} style={{ marginLeft: "10px" }}>
          Enviar Oferta
        </button>
        <button onClick={handleEnviarTransferencia} style={{ marginLeft: "10px" }}>
          Enviar Transferencia
        </button>
      </div>

      <div style={{ display: "flex", gap: "40px" }}>
        {/* ----------- Solicitudes ----------- */}
        <div>
          <h3>Solicitudes</h3>
          <ul>
            {solicitudes.map(s => (
              <li key={s.idSolicitud}>
                ID: {s.idSolicitud}, Org: {s.idOrganizacion}, Items:{" "}
                {s.donaciones.map(d => `${d.categoria} (${d.cantidad})`).join(", ")}
              </li>
            ))}
          </ul>
        </div>

        {/* ----------- Ofertas ----------- */}
        <div>
          <h3>Ofertas</h3>
          <ul>
            {ofertas.map(o => (
              <li key={o.idOferta}>
                ID: {o.idOferta}, Org: {o.idOrganizacionDonante}, Items:{" "}
                {o.donaciones.map(d => `${d.categoria} (${d.cantidad})`).join(", ")}
              </li>
            ))}
          </ul>
        </div>

        {/* ----------- Transferencias ----------- */}
        <div>
          <h3>Transferencias</h3>
          <ul>
            {transferencias.map(t => (
              <li key={t.idSolicitud}>
                ID Solicitud: {t.idSolicitud}, De: {t.idOrganizacion}, Items:{" "}
                {t.donaciones.map(d => `${d.categoria} (${d.cantidad})`).join(", ")}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}