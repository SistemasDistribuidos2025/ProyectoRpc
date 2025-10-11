import * as grpcDonaciones from "./donaciones_grpc_web_pb";
import * as messages from "./donaciones_pb";

const host = "http://localhost:8081"; // o el puerto que use tu proxy de gRPC-Web al envoy

// ✅ El nombre correcto del cliente es DonacionesServiceClient
const client = new grpcDonaciones.DonacionesServiceClient(host);

// -------------------- SOLICITUDES --------------------
export const enviarSolicitud = async (idOrganizacion, idSolicitud, donaciones) => {
  console.log("[enviarSolicitud] Llamado con:", { idOrganizacion, idSolicitud, donaciones });

  const request = new messages.SolicitudDonacionRequest();
  request.setIdorganizacion(idOrganizacion);
  request.setIdsolicitud(idSolicitud);

  donaciones.forEach((d) => {
    const item = new messages.ItemDonacion();
    item.setCategoria(d.categoria);
    item.setDescripcion(d.descripcion);
    if (d.cantidad !== undefined) item.setCantidad(d.cantidad);
    request.addDonaciones(item);
  });

  return new Promise((resolve, reject) => {
    client.enviarSolicitud(request, {}, (err, response) => {
      if (err) {
        console.error("[enviarSolicitud] Error:", err);
        reject(err);
      } else {
        console.log("[enviarSolicitud] Respuesta del servidor:", response.getMensaje());
        resolve(response.getMensaje());
      }
    });
  });
};

export const listarSolicitudes = async () => {
  const request = new messages.Vacio();
  return new Promise((resolve, reject) => {
    client.listarSolicitudes(request, {}, (err, response) => {
      if (err) reject(err);
      else
        resolve(
          response.getSolicitudesList().map((s) => ({
            idOrganizacion: s.getIdorganizacion(),
            idSolicitud: s.getIdsolicitud(),
            donaciones: s.getDonacionesList().map((d) => ({
              categoria: d.getCategoria(),
              descripcion: d.getDescripcion(),
              cantidad: d.getCantidad(),
            })),
          }))
        );
    });
  });
};

// -------------------- OFERTAS --------------------
export const enviarOferta = async (idOferta, idOrganizacionDonante, donaciones) => {
  console.log("[enviarOferta] Llamado con:", { idOferta, idOrganizacionDonante, donaciones });

  const request = new messages.OfertaDonacionRequest();
  request.setIdoferta(idOferta);
  request.setIdorganizaciondonante(idOrganizacionDonante);

  donaciones.forEach((d) => {
    const item = new messages.ItemDonacion();
    item.setCategoria(d.categoria);
    item.setDescripcion(d.descripcion);
    if (d.cantidad !== undefined) item.setCantidad(d.cantidad);
    request.addDonaciones(item);
  });

  return new Promise((resolve, reject) => {
    client.enviarOferta(request, {}, (err, response) => {
      if (err) {
        console.error("[enviarOferta] Error:", err);
        reject(err);
      } else {
        console.log("[enviarOferta] Respuesta del servidor:", response.getMensaje());
        resolve(response.getMensaje());
      }
    });
  });
};

export const listarOfertas = async () => {
  const request = new messages.Vacio();
  return new Promise((resolve, reject) => {
    client.listarOfertas(request, {}, (err, response) => {
      if (err) reject(err);
      else
        resolve(
          response.getOfertasList().map((o) => ({
            idOferta: o.getIdoferta(),
            idOrganizacionDonante: o.getIdorganizaciondonante(),
            donaciones: o.getDonacionesList().map((d) => ({
              categoria: d.getCategoria(),
              descripcion: d.getDescripcion(),
              cantidad: d.getCantidad(),
            })),
          }))
        );
    });
  });
};

// -------------------- TRANSFERENCIAS --------------------
export const enviarTransferencia = async (
  idSolicitud,
  idOrganizacionDonante,
  idOrganizacionReceptora,
  donaciones
) => {
  console.log("[enviarTransferencia] Llamado con:", { idSolicitud, idOrganizacionDonante, idOrganizacionReceptora, donaciones });

  const request = new messages.TransferenciaDonacionRequest();
  request.setIdsolicitud(idSolicitud);
  request.setIdorganizaciondonante(idOrganizacionDonante);
  request.setIdorganizacionreceptora(idOrganizacionReceptora);

  donaciones.forEach((d) => {
    const item = new messages.ItemDonacion();
    item.setCategoria(d.categoria);
    item.setDescripcion(d.descripcion);
    if (d.cantidad !== undefined) item.setCantidad(d.cantidad);
    request.addDonaciones(item);
  });

  return new Promise((resolve, reject) => {
    client.enviarTransferencia(request, {}, (err, response) => {
      if (err) {
        console.error("[enviarTransferencia] Error:", err);
        reject(err);
      } else {
        console.log("[enviarTransferencia] Respuesta del servidor:", response.getMensaje());
        resolve(response.getMensaje());
      }
    });
  });
};