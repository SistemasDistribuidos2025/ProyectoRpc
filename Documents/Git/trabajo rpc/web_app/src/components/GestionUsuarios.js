import React, { useEffect, useState } from "react";
import {
  listarUsuarios,
  altaUsuario,
  modificarUsuario,
  bajaUsuario,
  buscarUsuarioPorId,
  buscarUsuarioPorEmail,
  buscarUsuarioPorNombreUsuario,
} from "../servicios/grpcCliente";
import "./GestionUsuarios.css";

const GestionUsuarios = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [form, setForm] = useState({
    //id: null,
    nombre: "",
    apellido: "",
    nombreusuario: "",
    email: "",
    rol: "VOLUNTARIO",
    activo: true,
    telefono: "",
  });
  const [error, setError] = useState("");
  const [resultadoBusqueda, setResultadoBusqueda] = useState(null);

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      const lista = await listarUsuarios();
      setUsuarios(lista);
    } catch (err) {
      console.error("Error cargando usuarios:", err);
      setError(err.message || "Error cargando usuarios");
    }
  };

  const handleChange = (e) => {
    const value = e.target.type === "checkbox" ? e.target.checked : e.target.value;
    setForm({ ...form, [e.target.name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = { ...form }; // clonamos
      // Eliminamos clave del objeto por seguridad
      delete data.clave;

      if (form.id) {
      
        await modificarUsuario(data);
      } else {
        await altaUsuario(data);
      }
      resetForm();
      cargarUsuarios();
    } catch (err) {
      console.error("Error en alta/modificación:", err);
      setError(err.message || "Error en alta/modificación");
    }
  };

  const resetForm = () => {
    setForm({
      //id: null,
      nombre: "",
      apellido: "",
      nombreusuario: "",
      email: "",
      rol: "VOLUNTARIO",
      activo: true,
      telefono: "",
    });
  };

  const handleEditar = (usuario) => {
    setForm({ ...usuario }); // no mostramos clave
  };

  const handleDarDeBaja = async (id) => {
    try {
      await bajaUsuario(id);
      cargarUsuarios();
    } catch (err) {
      console.error("Error al dar de baja:", err);
      setError(err.message || "Error al dar de baja");
    }
  };

  // =============================
  // MÉTODOS DE BÚSQUEDA
  // =============================
  const [busquedaId, setBusquedaId] = useState("");  
  const buscarPorId = async (id) => {
  try {
      const u = await buscarUsuarioPorId(id);
      setResultadoBusqueda(u);
    } catch (err) {
      console.error("Error:", err);
      setError(err.message || "Error buscando por ID");
    }
  };

  const [busquedaEmail, setBusquedaEmail] = useState("");  
  const buscarPorEmail = async (email) => {
    try {
      const u = await buscarUsuarioPorEmail(email);
      setResultadoBusqueda(u);
    } catch (err) {
      setError(err.message || "Error buscando por email");
    }
  };

  const [busquedaUsuario, setBusquedaUsuario] = useState(""); 
  const buscarPorNombreUsuario = async (nombreUsuario) => {
    try {
      const u = await buscarUsuarioPorNombreUsuario(nombreUsuario);
      setResultadoBusqueda(u);
    } catch (err) {
      setError(err.message || "Error buscando por nombre de usuario");
    }
  };

  return (
    <div className="gestion-container">
      <h2>👥 Gestión de Usuarios</h2>
      {error && <p className="error-message">{error}</p>}

      {/* Formulario Alta/Modificar */}
      <form className="gestion-form" onSubmit={handleSubmit}>
        <input type="text" name="nombre" placeholder="Nombre" value={form.nombre} onChange={handleChange} required />
        <input type="text" name="apellido" placeholder="Apellido" value={form.apellido} onChange={handleChange} required />
        <input type="text" name="nombreusuario" placeholder="Nombre de usuario" value={form.nombreusuario} onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" value={form.email} onChange={handleChange} required />
        <input type="text" name="telefono" placeholder="Teléfono" value={form.telefono} onChange={handleChange} />
        <select name="rol" value={form.rol} onChange={handleChange}>
          <option value="PRESIDENTE">PRESIDENTE</option>
          <option value="VOCAL">VOCAL</option>
          <option value="COORDINADOR">COORDINADOR</option>
          <option value="VOLUNTARIO">VOLUNTARIO</option>
        </select>
        <label>
          Activo:
          <input type="checkbox" name="activo" checked={form.activo} onChange={handleChange} />
        </label>
        <button type="submit" onClick={()=>console.log(form)}>{form.id ? "Modificar Usuario" : "Agregar Usuario"}</button>
        {form.id && <button type="button" onClick={resetForm}>Cancelar</button>}
      </form>

      {/* Sección Búsquedas */}
      <div className="busquedas">
        <h3>🔍 Buscar Usuario</h3>

     
        <div className="input-busquedas">
          <input
            type="text"
            placeholder="ID del usuario"
            value={busquedaId}
            onChange={(e) => setBusquedaId(e.target.value)}
          />
          <button onClick={() => buscarPorId(busquedaId)}>Buscar por ID</button>
        </div>

       
        <div className="input-busquedas">
          <input
            type="email"
            placeholder="Email del usuario"
            value={busquedaEmail}
            onChange={(e) => setBusquedaEmail(e.target.value)}
          />
          <button onClick={() => buscarPorEmail(busquedaEmail)}>Buscar por Email</button>
        </div>

        
        <div className="input-busquedas">
          <input
            type="text"
            placeholder="Nombre de usuario"
            value={busquedaUsuario}
            onChange={(e) => setBusquedaUsuario(e.target.value)}
          />
          <button onClick={() => buscarPorNombreUsuario(busquedaUsuario)}>Buscar por Nombre de Usuario</button>
        </div>

        
        {resultadoBusqueda && (
          <div className="resultado">
            <h4>Resultado:</h4>
            <p>
              <b>Nombre:</b>{resultadoBusqueda.nombre} {resultadoBusqueda.apellido}  
              <br />
              <b>Email:</b> {resultadoBusqueda.email}
              <br />
              <b>Usuario:</b> {resultadoBusqueda.nombreusuario}
            </p>
          </div>
        )}

        {/* Error */}
        {error && <p style={{ color: "red" }}>{error}</p>}
      </div>


      {/* Tabla Usuarios */}
      <table className="usuarios-table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Usuario</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Activo</th>
            <th>Teléfono</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {usuarios.map((u) => (
            <tr key={u.id}>
              <td>{u.nombre}</td>
              <td>{u.apellido}</td>
              <td>{u.nombreusuario}</td>
              <td>{u.email}</td>
              <td>{u.rol}</td>
              <td>{u.activo ? "Sí" : "No"}</td>
              <td>{u.telefono}</td>
              <td>
                <button onClick={() => handleEditar(u)}>✏️ Editar</button>
                <button
                  onClick={() => handleDarDeBaja(u.id)}
                  className={u.activo ? "btn-inactivar" : "btn-inactivo"} 
                >
                  ⏸️ Inactivar
                </button>
              </td>

            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default GestionUsuarios;