import * as grpcDonaciones from "./donaciones_grpc_web_pb";
import * as messages from "./donaciones_pb";

const host = "http://localhost:8081"; 
const client = new grpcDonaciones.DonacionesServiceClient(host);

// -------------------- SOLICITUDES --------------------
export const enviarSolicitud = async (idOrganizacion, idSolicitud, donaciones) => {
  console.log("[enviarSolicitud] Llamado con:", { idOrganizacion, idSolicitud, donaciones });

  const request = new messages.SolicitudDonacionRequest();
  request.setIdorganizacion(String(idOrganizacion));
  request.setIdsolicitud(String(idSolicitud));

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
            donaciones: (s.getDonacionesList() || []).map((d) => ({
              categoria: d.getCategoria ? d.getCategoria() : d.categoria || "",
              descripcion: d.getDescripcion ? d.getDescripcion() : d.descripcion || "",
              cantidad: d.getCantidad ? d.getCantidad() : d.cantidad || 0,
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
  request.setIdoferta(String(idOferta));
  request.setIdorganizaciondonante(String(idOrganizacionDonante));

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
            donaciones: (o.getDonacionesList() || []).map((d) => ({
              categoria: d.getCategoria ? d.getCategoria() : d.categoria || "",
              descripcion: d.getDescripcion ? d.getDescripcion() : d.descripcion || "",
              cantidad: d.getCantidad ? d.getCantidad() : d.cantidad || 0,
            })),
          }))
        );
    });
  });
};

// -------------------- TRANSFERENCIAS --------------------
export async function enviarTransferencia(idSolicitud, idOrganizacionDonante, idOrganizacionReceptora, donaciones) {
  const donacionesConCantidad = (donaciones || []).map(d => ({
    categoria: d.categoria,
    descripcion: d.descripcion,
    cantidad: d.cantidad || 0
  }));

  const payload = { idSolicitud, idOrganizacionDonante, idOrganizacionReceptora, donaciones: donacionesConCantidad };
  console.log("Enviando transferencia:", payload);

  const response = await fetch("http://localhost:8080/transferencias", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!response.ok) throw new Error("Error al enviar transferencia");
  return await response.text();
}

// -------------------- BAJA SOLICITUD --------------------
export const darBajaSolicitud = async (idOrganizacion, idSolicitud) => {
  const request = new messages.BajaSolicitudRequest(); 
  request.setIdorganizacion(idOrganizacion);
  request.setIdsolicitud(idSolicitud);

  return new Promise((resolve, reject) => {
    client.bajaSolicitud(request, {}, (err, response) => {
      if (err) {
        console.error("[bajaSolicitud] Error:", err);
        reject(err);
      } else {
        console.log("[bajaSolicitud] Respuesta del servidor:", response.getMensaje());
        resolve(response.getMensaje());
      }
    });
  });
};