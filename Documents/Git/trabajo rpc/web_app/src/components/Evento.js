import React, { useEffect, useState } from "react";
import {
  listarEventos,
  listarUsuarios,
  listarInventario,
  asignarParticipantesEvento,
  quitarParticipantesEvento,
  registrarDonacionEvento,
  altaEvento,
  modificarEvento,
  bajaEvento
} from "../servicios/grpcCliente";
import "./Evento.css";

const Evento = () => {
  const [eventos, setEventos] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [inventario, setInventario] = useState([]);
  const [selectedEvento, setSelectedEvento] = useState(null);
  const [selectedUsuario, setSelectedUsuario] = useState(null);
  const [selectedItem, setSelectedItem] = useState(null);
  const [cantidadDonacion, setCantidadDonacion] = useState(0);

  // Datos para crear/modificar eventos
  const [nuevoNombre, setNuevoNombre] = useState("");
  const [nuevaDescripcion, setNuevaDescripcion] = useState("");
  const [nuevaFechaHora, setNuevaFechaHora] = useState("");

  // Cargar eventos, usuarios e inventario
  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    try {
      const listaEventos = await listarEventos();
      setEventos(listaEventos);

      const listaUsuarios = await listarUsuarios();
      setUsuarios(listaUsuarios);

      const listaInventario = await listarInventario();
      setInventario(listaInventario);
    } catch (err) {
      console.error("Error cargando datos:", err);
    }
  };

  const handleCrearEvento = async () => {
  if (!nuevoNombre || !nuevaDescripcion || !nuevaFechaHora) return;
  try {
    // Agregar segundos al string de fecha
    const fechaConSegundos = `${nuevaFechaHora}:00`;

    await altaEvento({
      nombreEvento: nuevoNombre,
      descripcion: nuevaDescripcion,
      fechaHoraEvento: fechaConSegundos, // <<== usar fechaConSegundos
      participantesEvento: []
    });

    await cargarDatos();
    alert("Evento creado!");
    setNuevoNombre(""); 
    setNuevaDescripcion(""); 
    setNuevaFechaHora("");
  } catch (err) {
    console.error(err);
    alert("Error creando evento");
  }
};

  const handleModificarEvento = async (evento) => {
    if (!evento.id) return;
    try {
      await modificarEvento({
        id: evento.id,
        nombreEvento: nuevoNombre || evento.nombreEvento,
        descripcion: nuevaDescripcion || evento.descripcion,
        fechaHoraEvento: nuevaFechaHora || evento.fechaHoraEvento
      });
      await cargarDatos();
      alert("Evento modificado!");
    } catch (err) {
      console.error(err);
      alert("Error modificando evento");
    }
  };

  const handleEliminarEvento = async (eventoId) => {
    try {
      await bajaEvento(eventoId);
      await cargarDatos();
      alert("Evento eliminado!");
    } catch (err) {
      console.error(err);
      alert("Error eliminando evento");
    }
  };

  // --- Participantes ---
  const handleAsignarParticipante = async () => {
    if (!selectedEvento || !selectedUsuario) return;
    try {
      await asignarParticipantesEvento(selectedEvento.id, [selectedUsuario.id]);
      await cargarDatos();
      alert("Participante asignado!");
    } catch (err) {
      console.error(err);
      alert("Error asignando participante");
    }
  };

  const handleQuitarParticipante = async () => {
    if (!selectedEvento || !selectedUsuario) return;
    try {
      await quitarParticipantesEvento(selectedEvento.id, [selectedUsuario.id]);
      await cargarDatos();
      alert("Participante quitado!");
    } catch (err) {
      console.error(err);
      alert("Error quitando participante");
    }
  };

  // --- Donaciones ---
  const handleRegistrarDonacion = async () => {
    if (!selectedEvento || !selectedItem || cantidadDonacion <= 0) return;
    try {
      await registrarDonacionEvento(
        selectedEvento.id,
        selectedItem.id,
        cantidadDonacion,
        1 // ID del usuario que registra la donación
      );
      await cargarDatos();
      alert("Donación registrada!");
      setCantidadDonacion(0);
    } catch (err) {
      console.error(err);
      alert("Error registrando donación");
    }
  };

  return (
    <div className="evento-card">
      <h2 className="section-title">📅 Gestión de Eventos</h2>

      {/* Crear Evento */}
      <div className="crear-evento">
        <h3>➕ Crear Evento</h3>
        <input placeholder="Nombre" value={nuevoNombre} onChange={(e) => setNuevoNombre(e.target.value)} />
        <input placeholder="Descripción" value={nuevaDescripcion} onChange={(e) => setNuevaDescripcion(e.target.value)} />
        <input type="datetime-local" value={nuevaFechaHora} onChange={(e) => setNuevaFechaHora(e.target.value)} />
        <button onClick={handleCrearEvento}>Crear</button>
      </div>

      {/* Lista de eventos */}
      <ul className="evento-list">
        {eventos.map((e) => (
          <li key={e.id} className="evento-item">
            <div className="evento-header">
              <strong>{e.nombreEvento}</strong>
              <span className="evento-date">{e.fechaHoraEvento}</span>
              <button onClick={() => handleModificarEvento(e)}>✏️ Modificar</button>
              <button onClick={() => handleEliminarEvento(e.id)}>🗑️ Eliminar</button>
            </div>
            <p className="evento-desc">{e.descripcion}</p>

            {e.participantesEvento?.length > 0 && (
              <ul className="participantes-list">
                {e.participantesEvento.map((p) => (
                  <li key={p.id}>👤 {p.nombre} {p.apellido}</li>
                ))}
              </ul>
            )}

            {/* Asignar/Quitar Participantes */}
            <div className="evento-actions">
              <select onChange={(ev) => setSelectedUsuario(usuarios.find(u => u.id === parseInt(ev.target.value)))}
                      defaultValue="">
                <option value="" disabled>Seleccionar usuario</option>
                {usuarios.map((u) => (
                  <option key={u.id} value={u.id}>{u.nombre} {u.apellido} ({u.rol})</option>
                ))}
              </select>
              <button onClick={() => { setSelectedEvento(e); handleAsignarParticipante(); }}>➕ Asignar</button>
              <button onClick={() => { setSelectedEvento(e); handleQuitarParticipante(); }}>➖ Quitar</button>
            </div>

            {/* Registrar donación */}
            <div className="donacion-form">
              <select onChange={(ev) => setSelectedItem(inventario.find(i => i.id === parseInt(ev.target.value)))}
                      defaultValue="">
                <option value="" disabled>Seleccionar item</option>
                {inventario.map((i) => (
                  <option key={i.id} value={i.id}>{i.descripcion} ({i.cantidad} disponibles)</option>
                ))}
              </select>
              <input type="number" min="1" value={cantidadDonacion} onChange={(e) => setCantidadDonacion(parseInt(e.target.value))} placeholder="Cantidad" />
              <button onClick={() => { setSelectedEvento(e); handleRegistrarDonacion(); }}>💝 Donar</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Evento;
