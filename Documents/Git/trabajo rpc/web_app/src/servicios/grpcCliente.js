import { UsuarioServiceClient, EventoSolidarioServiceClient, InventarioDonacionesServiceClient } from "./usuario_grpc_web_pb";
import {
  Usuario,
  UsuarioIdRequest,
  UsuarioEmailRequest,
  UsuarioNombreRequest,
  LoginRequest,
} from "./usuario_pb";
import { Empty } from 'google-protobuf/google/protobuf/empty_pb';


const client = new UsuarioServiceClient('http://localhost:8081', null, null);
const eventoClient = new EventoSolidarioServiceClient('http://localhost:8081', null, null);
const inventarioClient = new  InventarioDonacionesServiceClient('http://localhost:8081', null, null);

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

// --- Métodos ---

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

//listarUsuarios: obtiene todos los usuarios

export const listarUsuarios = () => {
  return new Promise((resolve, reject) => {
    const request = new Empty(); 
    client.listarTodos(request, {}, (err, response) => {
      if (err) return reject(err);
      const usuarios = response.getUsuariosList().map(u => u.toObject());
      resolve(usuarios);
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

// --- Alta Usuario (sin clave, se genera en servidor) ---
export const altaUsuario = (usuarioData) => {
  return new Promise((resolve, reject) => {
    const usuario = new Usuario();
    //if (usuarioData.id !== undefined) safeSet(usuario, "id", usuarioData.id);
    if (usuarioData.nombre !== undefined) safeSet(usuario, "nombre", usuarioData.nombre);
    if (usuarioData.apellido !== undefined) safeSet(usuario, "apellido", usuarioData.apellido);
    if (usuarioData.nombreusuario !== undefined) safeSet(usuario, "nombreusuario", usuarioData.nombreusuario); //nombreusuario, en minuscula, no nombreUsuario
    if (usuarioData.email !== undefined) safeSet(usuario, "email", usuarioData.email);
    if (usuarioData.rol !== undefined) safeSet(usuario, "rol", usuarioData.rol);
    if (usuarioData.activo !== undefined) safeSet(usuario, "activo", usuarioData.activo);
    if (usuarioData.telefono !== undefined) safeSet(usuario, "telefono", usuarioData.telefono);

    client.altaUsuario(usuario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario ? response.getUsuario().toObject() : response.toObject());
    });
  });
};

// --- Modificar Usuario (sin modificar clave) ---
export const modificarUsuario = (usuarioData) => {
  return new Promise((resolve, reject) => {
    const usuario = new Usuario();
    if (usuarioData.id !== undefined) safeSet(usuario, "id", usuarioData.id);
    if (usuarioData.nombre !== undefined) safeSet(usuario, "nombre", usuarioData.nombre);
    if (usuarioData.apellido !== undefined) safeSet(usuario, "apellido", usuarioData.apellido);
    if (usuarioData.nombreUsuario !== undefined) safeSet(usuario, "nombreusuario", usuarioData.nombreUsuario); //nombreusuario, en minuscula, no nombreUsuario
    if (usuarioData.email !== undefined) safeSet(usuario, "email", usuarioData.email);
    if (usuarioData.rol !== undefined) safeSet(usuario, "rol", usuarioData.rol);
    if (usuarioData.activo !== undefined) safeSet(usuario, "activo", usuarioData.activo);
    if (usuarioData.telefono !== undefined) safeSet(usuario, "telefono", usuarioData.telefono);

    client.modificarUsuario(usuario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario ? response.getUsuario().toObject() : response.toObject());
    });
  });
};

// --- Baja Usuario ---
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



//listarEventos: obtiene todos los eventos

export const listarEventos = () => {
  return new Promise((resolve, reject) => {
    const request = new Empty();

    eventoClient.listarEventos(request, {}, (err, response) => {
      if (err) return reject(err);

      const eventos = response.getEventosList().map(e => e.toObject());
      resolve(eventos);
    });
  });
};


//listarInventario: obtiene todos los elementos del inventario

export const listarInventario = () => {
  return new Promise((resolve, reject) => {
    const request = new Empty();

    inventarioClient.listarTodos (request, {}, (err, response) => {
      if (err) return reject(err);

      const inventario = response.getInventariosList().map(i => i.toObject());
      resolve(inventario);
    });
  });
};