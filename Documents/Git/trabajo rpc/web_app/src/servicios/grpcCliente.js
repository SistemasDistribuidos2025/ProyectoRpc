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
    request.setNombreUsuario(nombreUsuario);

    client.buscarPorNombreUsuario(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario().toObject());
    });
  });
};

//altaUsuario: crea un usuario
export const altaUsuario = (usuarioData) => {
  return new Promise((resolve, reject) => {
    const usuario = new Usuario();
    Object.entries(usuarioData).forEach(([key, value]) => {
      usuario[`set${capitalize(key)}`](value);
    });

    client.altaUsuario(usuario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario().toObject());
    });
  });
};

export const modificarUsuario = (usuarioData) => {
  return new Promise((resolve, reject) => {
    const usuario = new Usuario();
    Object.entries(usuarioData).forEach(([key, value]) => {
      usuario[`set${capitalize(key)}`](value);
    });

    client.modificarUsuario(usuario, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario().toObject());
    });
  });
};

export const bajaUsuario = (id) => {
  return new Promise((resolve, reject) => {
    const request = new UsuarioIdRequest();
    request.setId(id);

    client.bajaUsuario(request, {}, (err, response) => {
      if (err) return reject(err);
      resolve(response.getUsuario().toObject());
    });
  });
};

const capitalize = (s) => s.charAt(0).toUpperCase() + s.slice(1);

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