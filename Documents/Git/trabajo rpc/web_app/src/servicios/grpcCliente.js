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
  InventarioIdRequest,
  CategoriaDonacion,
  EventoSolidario,
  EventoIdRequest,
  EventoListResponse,
  EventoParticipantesRequest,
  DonacionEventoRequest
} from "./usuario_pb";


import { Empty } from 'google-protobuf/google/protobuf/empty_pb';

import { Timestamp } from 'google-protobuf/google/protobuf/timestamp_pb';

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

const stringToTimestamp = (dateString) => {
  const date = new Date(dateString);
  const timestamp = new Timestamp();
  timestamp.setSeconds(Math.floor(date.getTime() / 1000));
  timestamp.setNanos((date.getTime() % 1000) * 1e6);
  return timestamp;
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

    const categoriaEnumMap = {
      0: CategoriaDonacion.ROPA,
      1: CategoriaDonacion.ALIMENTOS,
      2: CategoriaDonacion.JUGUETES,
      3: CategoriaDonacion.UTILES_ESCOLARES,
    };

    if (inventarioData.categoria !== undefined) {
      inventario.setCategoria(categoriaEnumMap[inventarioData.categoria]);
    }

    // Setear campos opcionales
    if (inventarioData.descripcion) inventario.setDescripcion(inventarioData.descripcion);
    if (inventarioData.cantidad !== undefined) inventario.setCantidad(inventarioData.cantidad);
    inventario.setEliminado(false);

    // **IDs de usuario**: asegurar que sean números válidos
    if (inventarioData.usuarioAltaId && inventarioData.usuarioAltaId > 0) {
      inventario.setUsuarioaltaid(inventarioData.usuarioAltaId);
    } else {
      return reject(new Error("UsuarioAltaId inválido"));
    }

    if (inventarioData.usuarioModificadoId && inventarioData.usuarioModificadoId > 0) {
      inventario.setUsuariomodificadoid(inventarioData.usuarioModificadoId);
    }

    console.log("Objeto Inventario a enviar a gRPC:", inventario.toObject());

    inventarioClient.altaInventario(inventario, {}, (err, response) => {
      if (err) {
        console.error("Error gRPC:", err);
        return reject(err);
      }
      resolve(response.toObject());
    });
  });
};

export const modificarInventario = (inventarioData) => {
  return new Promise((resolve, reject) => {
    const inventario = new InventarioDonaciones();

    if (!inventarioData.id) return reject(new Error("ID de inventario obligatorio"));

    inventario.setId(inventarioData.id);
    if (inventarioData.descripcion) inventario.setDescripcion(inventarioData.descripcion);
    if (inventarioData.cantidad !== undefined) inventario.setCantidad(inventarioData.cantidad);

    if (inventarioData.usuarioModificadoId && inventarioData.usuarioModificadoId > 0) {
      inventario.setUsuariomodificadoid(inventarioData.usuarioModificadoId);
    } else {
      return reject(new Error("UsuarioModificadoId inválido"));
    }

    inventarioClient.modificarInventario(inventario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};


export const bajaInventario = (id, usuarioModificadoId) => {
  return new Promise((resolve, reject) => {
    const request = new InventarioIdRequest();
    request.setId(id);
    if (usuarioModificadoId !== undefined) request.setUsuariomodificadoid(usuarioModificadoId);

    inventarioClient.bajaInventario(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response); 
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

// Buscar evento por ID
export const buscarEventoPorId = (eventoId) => {
  return new Promise((resolve, reject) => {
    const request = new EventoIdRequest();
    request.setId(eventoId);

    eventoClient.buscarEventoPorId(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};

// Crear un nuevo evento
export const altaEvento = (eventoData) => {
  return new Promise((resolve, reject) => {
    const evento = new EventoSolidario();
    if (eventoData.nombreEvento) evento.setNombreevento(eventoData.nombreEvento);
    if (eventoData.descripcion) evento.setDescripcion(eventoData.descripcion);
    if (eventoData.fechaHoraEvento) {
      evento.setFechahoraevento(stringToTimestamp(eventoData.fechaHoraEvento));
    }

    eventoClient.altaEvento(evento, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};

// Modificar un evento existente
export const modificarEvento = (eventoData) => {
  return new Promise((resolve, reject) => {
    if (!eventoData.id) return reject(new Error("ID de evento obligatorio"));
    const evento = new EventoSolidario();
    evento.setId(eventoData.id);
    if (eventoData.nombreEvento) evento.setNombreevento(eventoData.nombreEvento);
    if (eventoData.descripcion) evento.setDescripcion(eventoData.descripcion);
    if (eventoData.fechaHoraEvento) {
      evento.setFechahoraevento(stringToTimestamp(eventoData.fechaHoraEvento));
    }

    eventoClient.modificarEvento(evento, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};

// Eliminar un evento
export const bajaEvento = (eventoId) => {
  return new Promise((resolve, reject) => {
    const request = new EventoIdRequest();
    request.setId(eventoId);

    eventoClient.bajaEvento(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response); // Empty
    });
  });
};

export const asignarParticipantesEvento = (eventoId, usuarioIds, usuarioLogueado) => {
  return new Promise((resolve, reject) => {
    if (!eventoId || !usuarioIds?.length || !usuarioLogueado) 
      return reject(new Error("Datos incompletos para asignar participantes"));

    // Filtrado opcional: si es VOLUNTARIO, solo puede agregarse a sí mismo
    const idsFiltrados = usuarioLogueado.rol === "VOLUNTARIO"
      ? usuarioIds.filter(id => id === usuarioLogueado.id)
      : usuarioIds;

    if (!idsFiltrados.length) return reject(new Error("VOLUNTARIO solo puede agregarse a sí mismo"));

    const request = new (require("./usuario_pb").EventoParticipantesRequest)();
    request.setEventoid(eventoId);
    request.setUsuarioidsList(idsFiltrados);
    request.setRolsolicitante(usuarioLogueado.rol);

    eventoClient.asignarParticipantes(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};

export const quitarParticipantesEvento = (eventoId, usuarioIds, usuarioLogueado) => {
  return new Promise((resolve, reject) => {
    if (!eventoId || !usuarioIds?.length || !usuarioLogueado) 
      return reject(new Error("Datos incompletos para quitar participantes"));

    const idsFiltrados = usuarioLogueado.rol === "VOLUNTARIO"
      ? usuarioIds.filter(id => id === usuarioLogueado.id)
      : usuarioIds;

    if (!idsFiltrados.length) return reject(new Error("VOLUNTARIO solo puede quitarse a sí mismo"));

    const request = new (require("./usuario_pb").EventoParticipantesRequest)();
    request.setEventoid(eventoId);
    request.setUsuarioidsList(idsFiltrados);
    request.setRolsolicitante(usuarioLogueado.rol);

    eventoClient.quitarParticipantes(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject());
    });
  });
};

export const registrarDonacionEvento = (eventoId, inventarioId, cantidad, usuarioLogueado) => {
  return new Promise((resolve, reject) => {
    if (!eventoId || !inventarioId || !usuarioLogueado || cantidad <= 0)
      return reject(new Error("Datos incompletos o cantidad inválida"));

    if (!(usuarioLogueado.rol === "PRESIDENTE" || usuarioLogueado.rol === "COORDINADOR")) {
      return reject(new Error("Solo PRESIDENTE o COORDINADOR pueden registrar donaciones"));
    }

    const request = new (require("./usuario_pb").DonacionEventoRequest)();
    request.setEventoid(eventoId);
    request.setInventarioid(inventarioId);
    request.setCantidad(cantidad);
    request.setUsuarioid(usuarioLogueado.id);

    eventoClient.registrarDonacionEvento(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.toObject ? response.toObject() : response);
    });
  });
};


