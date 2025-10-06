import { 
  UsuarioServiceClient, 
  EventoSolidarioServiceClient, 
  InventarioDonacionesServiceClient 
} from "./usuario_grpc_web_pb";

import {
  Usuario,
  UsuarioIdRequest,
  UsuarioEmailRequest,
  UsuarioNombreRequest,
  LoginRequest,
  InventarioDonaciones,
  InventarioIdRequest
} from "./usuario_pb";

import { Empty } from 'google-protobuf/google/protobuf/empty_pb';

// --- Clients ---
const client = new UsuarioServiceClient('http://localhost:8081', null, null);
const eventoClient = new EventoSolidarioServiceClient('http://localhost:8081', null, null);
const inventarioClient = new InventarioDonacionesServiceClient('http://localhost:8081', null, null);

// --- Helpers ---
const capitalize = (s) => s.charAt(0).toUpperCase() + s.slice(1);

const safeSet = (msgInstance, key, value) => {
  const candidates = [
    `set${capitalize(key)}`,
    `set${key}`,
    `set${capitalize(key.replace(/_([a-z])/g, (_, c) => c.toUpperCase()))}`,
    `set${key.replace(/_([a-z])/g, (_, c) => c.toUpperCase())}`
  ];
  for (const name of candidates) {
    if (typeof msgInstance[name] === "function") {
      msgInstance[name](value);
      return true;
    }
  }
  console.warn(`Setter no encontrado para campo '${key}'. Intentados: ${candidates.join(", ")}`);
  return false;
};

// --- Métodos Usuario ---
export const loginGrpcWeb = (identificador, password) => {
  return new Promise((resolve, reject) => {
    const request = new LoginRequest();
    request.setIdentificador(identificador);
    request.setPassword(password);

    client.login(request, {}, (err, response) => {
      if (err) return reject(err);
      if (response.getSuccess()) {
        resolve(response.getUsuario().toObject());
      } else {
        reject(new Error(response.getMessage()));
      }
    });
  });
};

export const listarUsuarios = () => {
  return new Promise((resolve, reject) => {
    const request = new Empty(); 
    client.listarTodos(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuariosList().map(u => u.toObject()));
    });
  });
};

export const buscarUsuarioPorId = (id) => {
  return new Promise((resolve, reject) => {
    const request = new UsuarioIdRequest();
    request.setId(id);
    client.buscarPorId(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario().toObject());
    });
  });
};

export const buscarUsuarioPorEmail = (email) => {
  return new Promise((resolve, reject) => {
    const request = new UsuarioEmailRequest();
    request.setEmail(email);
    client.buscarPorEmail(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario().toObject());
    });
  });
};

export const buscarUsuarioPorNombreUsuario = (nombreUsuario) => {
  return new Promise((resolve, reject) => {
    const request = new UsuarioNombreRequest();
    request.setNombreusuario(nombreUsuario);
    client.buscarPorNombreUsuario(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario().toObject());
    });
  });
};

export const altaUsuario = (usuarioData) => {
  return new Promise((resolve, reject) => {
    const usuario = new Usuario();
    if (usuarioData.nombre) safeSet(usuario, "nombre", usuarioData.nombre);
    if (usuarioData.apellido) safeSet(usuario, "apellido", usuarioData.apellido);
    if (usuarioData.nombreusuario) safeSet(usuario, "nombreusuario", usuarioData.nombreusuario);
    if (usuarioData.email) safeSet(usuario, "email", usuarioData.email);
    if (usuarioData.rol) safeSet(usuario, "rol", usuarioData.rol);
    if (usuarioData.activo !== undefined) safeSet(usuario, "activo", usuarioData.activo);
    if (usuarioData.telefono) safeSet(usuario, "telefono", usuarioData.telefono);

    client.altaUsuario(usuario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario ? response.getUsuario().toObject() : response.toObject());
    });
  });
};

export const modificarUsuario = (usuarioData) => {
  return new Promise((resolve, reject) => {
    const usuario = new Usuario();
    if (usuarioData.id) safeSet(usuario, "id", usuarioData.id);
    if (usuarioData.nombre) safeSet(usuario, "nombre", usuarioData.nombre);
    if (usuarioData.apellido) safeSet(usuario, "apellido", usuarioData.apellido);
    if (usuarioData.nombreusuario) safeSet(usuario, "nombreusuario", usuarioData.nombreusuario);
    if (usuarioData.email) safeSet(usuario, "email", usuarioData.email);
    if (usuarioData.rol) safeSet(usuario, "rol", usuarioData.rol);
    if (usuarioData.activo !== undefined) safeSet(usuario, "activo", usuarioData.activo);
    if (usuarioData.telefono) safeSet(usuario, "telefono", usuarioData.telefono);

    client.modificarUsuario(usuario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario ? response.getUsuario().toObject() : response.toObject());
    });
  });
};

export const bajaUsuario = (id) => {
  return new Promise((resolve, reject) => {
    const request = new UsuarioIdRequest();
    request.setId(id);
    client.bajaUsuario(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario ? response.getUsuario().toObject() : response.toObject());
    });
  });
};

// --- Métodos Evento ---
export const listarEventos = () => {
  return new Promise((resolve, reject) => {
    const request = new Empty();
    eventoClient.listarEventos(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getEventosList().map(e => e.toObject()));
    });
  });
};

// --- Métodos Inventario ---
export const listarInventario = () => {
  return new Promise((resolve, reject) => {
    const request = new Empty();
    inventarioClient.listarTodos(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getInventariosList().map(i => i.toObject()));
    });
  });
};

export const altaInventario = (inventarioData) => {
  return new Promise((resolve, reject) => {
    const inventario = new InventarioDonaciones();
    if (inventarioData.categoria !== undefined) inventario.setCategoria(inventarioData.categoria);
    if (inventarioData.descripcion !== undefined) inventario.setDescripcion(inventarioData.descripcion);
    if (inventarioData.cantidad !== undefined) inventario.setCantidad(inventarioData.cantidad);
    inventario.setEliminado(false);
    if (inventarioData.usuarioAltaId !== undefined) inventario.setUsuarioAltaId(inventarioData.usuarioAltaId);

    inventarioClient.altaInventario(inventario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};

export const modificarInventario = (inventarioData) => {
  return new Promise((resolve, reject) => {
    const inventario = new InventarioDonaciones();
    if (inventarioData.id !== undefined) inventario.setId(inventarioData.id);
    if (inventarioData.descripcion !== undefined) inventario.setDescripcion(inventarioData.descripcion);
    if (inventarioData.cantidad !== undefined) inventario.setCantidad(inventarioData.cantidad);
    if (inventarioData.usuarioModificadoId !== undefined) inventario.setUsuarioModificadoId(inventarioData.usuarioModificadoId);

    inventarioClient.modificarInventario(inventario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};

export const bajaInventario = (id, usuarioModificadoId) => {
  return new Promise((resolve, reject) => {
    const inventario = new InventarioDonaciones();
    inventario.setId(id);
    inventario.setEliminado(true);
    if (usuarioModificadoId !== undefined) inventario.setUsuarioModificadoId(usuarioModificadoId);

    inventarioClient.modificarInventario(inventario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};
