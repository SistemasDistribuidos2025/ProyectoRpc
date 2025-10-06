import React, { useEffect, useState } from "react"; 
import { 
  listarInventario, 
  altaInventario, 
  modificarInventario, 
  bajaInventario 
} from "../servicios/grpcCliente";
import "./Inventario.css";

// Mapeo de categorías
const categoriaMap = {
  0: "Ropa",
  1: "Alimentos",
  2: "Juguetes",
  3: "Útiles escolares"
};

const Inventario = ({ usuarioLogueado }) => {
  const [inventario, setInventario] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [formData, setFormData] = useState({ id: null, categoria: 0, descripcion: "", cantidad: 0 });
  const [modoEdicion, setModoEdicion] = useState(false);
  const [mensaje, setMensaje] = useState(null);

  const cargarInventario = async () => {
    setCargando(true);
    try {
      const lista = await listarInventario();
      setInventario(lista);
    } catch (err) {
      console.error("Error cargando Inventario:", err);
      setMensaje({ tipo: "error", texto: "Error cargando inventario" });
    } finally {
      setCargando(false);
    }
  };

  useEffect(() => { cargarInventario(); }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: name === "cantidad" ? Number(value) : value }));
  };

  const mostrarMensaje = (tipo, texto) => {
    setMensaje({ tipo, texto });
    setTimeout(() => setMensaje(null), 3000);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!usuarioLogueado) return mostrarMensaje("error", "Debe iniciar sesión");
    if (formData.cantidad < 0) return mostrarMensaje("error", "Cantidad no puede ser negativa");

    try {
      if (modoEdicion) {
        await modificarInventario({ ...formData, usuarioModificadoId: usuarioLogueado.id });
        mostrarMensaje("exito", "Elemento modificado correctamente");
      } else {
        await altaInventario({ ...formData, usuarioAltaId: usuarioLogueado.id });
        mostrarMensaje("exito", "Elemento agregado correctamente");
      }
      setFormData({ id: null, categoria: 0, descripcion: "", cantidad: 0 });
      setModoEdicion(false);
      cargarInventario();
    } catch (err) {
      console.error(err);
      mostrarMensaje("error", "Error al guardar inventario");
    }
  };

  const handleEditar = (item) => {
    setFormData({ id: item.id, categoria: item.categoria ?? 0, descripcion: item.descripcion ?? "", cantidad: item.cantidad ?? 0 });
    setModoEdicion(true);
  };

  const handleEliminar = async (id) => {
    if (!usuarioLogueado) return mostrarMensaje("error", "Debe iniciar sesión");
    if (!window.confirm("¿Seguro que deseas eliminar este ítem?")) return;

    try {
      await bajaInventario(id, usuarioLogueado.id);
      mostrarMensaje("exito", "Elemento eliminado correctamente");
      cargarInventario();
    } catch (err) {
      console.error(err);
      mostrarMensaje("error", "Error eliminando inventario");
    }
  };

  const puedeEditar = true; // Temporal: siempre muestra formulario

  if (cargando) return <p style={{ textAlign: "center" }}>Cargando inventario...</p>;

  return (
    <div className="inventario-card">
      <h2 className="section-title">📦 Lista de Inventario</h2>

      {mensaje && (
        <p className={mensaje.tipo === "exito" ? "mensaje-exito" : "mensaje-error"}>{mensaje.texto}</p>
      )}

      {puedeEditar && (
        <form onSubmit={handleSubmit}>
          <select name="categoria" value={formData.categoria} onChange={handleChange}>
            {Object.entries(categoriaMap).map(([key, value]) => (
              <option key={key} value={key}>{value}</option>
            ))}
          </select>
          <input type="text" name="descripcion" placeholder="Descripción" value={formData.descripcion} onChange={handleChange} required />
          <input type="number" name="cantidad" placeholder="Cantidad" value={formData.cantidad} onChange={handleChange} min={0} required />
          <button type="submit">{modoEdicion ? "Modificar" : "Agregar"}</button>
          {modoEdicion && (
            <button type="button" onClick={() => { setModoEdicion(false); setFormData({ id: null, categoria: 0, descripcion: "", cantidad: 0 }); }}>
              Cancelar
            </button>
          )}
        </form>
      )}

      {inventario.length === 0 ? (
        <p style={{ textAlign: "center" }}>No hay elementos en el inventario.</p>
      ) : (
        <ul className="inventario-list">
          {inventario.map(i => (
            <li key={i.id} className="inventario-item">
              <div className="inventario-header">
                <strong>{categoriaMap[i.categoria] || "Desconocida"}</strong>
                <span className="inventario-cantidad">Cantidad: {i.cantidad}</span>
              </div>
              {i.descripcion && <p className="inventario-desc">{i.descripcion}</p>}
              {puedeEditar && (
                <div className="botones-item">
                  <button type="button" onClick={() => handleEditar(i)}>Editar</button>
                  <button type="button" onClick={() => handleEliminar(i.id)}>Eliminar</button>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default Inventario;
