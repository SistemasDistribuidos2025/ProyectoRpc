
import { EventosServiceClient } from "./donaciones_grpc_web_pb";
import { EventoRequest, BajaEventoRequest, Vacio, AdhesionRequest, Voluntario } from "./donaciones_pb";

const client = new EventosServiceClient("http://localhost:8081");

export const listarEventosExternos = () => {
    return new Promise((resolve, reject) => {
        const request = new Vacio();
        client.listarEventosExternos(request, {}, (err, response) => {
            if (err) {
                console.error("Error al listar eventos:", err);
                return reject(err);
            }
            resolve(response.getEventosList().map((e) => e.toObject()));
        });
    });
};


export const listarEventosPropios = () => {
    return new Promise((resolve, reject) => {
        const request = new Vacio();
        client.listarEventosPropios(request, {}, (err, response) => {
            if (err) {
                console.error("Error al listar eventos:", err);
                return reject(err);
            }
            resolve(response.getEventosList().map((e) => e.toObject()));
        });
    });
};

export const darBajaEvento = (idEvento) => {
    return new Promise((resolve, reject) => {
        const request = new BajaEventoRequest();
        request.setIdorganizacion("ONG001");
        request.setIdevento(idEvento);

        client.bajaEvento(request, {}, (err, response) => {
            if (err) {
                console.error("Error al dar de baja evento:", err);
                return reject(err);
            }
            resolve(response.getMensaje());
        });
    });
};

export const publicarEvento = (evento) => {
    return new Promise((resolve, reject) => {
        const request = new EventoRequest();
        request.setIdorganizacion(evento.idOrganizacion);
        request.setIdevento(evento.idEvento);
        request.setNombre(evento.nombre);
        request.setDescripcion(evento.descripcion);
        request.setFechahora(evento.fechaHora);

        client.publicarEvento(request, {}, (err, response) => {
            if (err) return reject(err);
            resolve(response.getMensaje());
        });
    });
};
export const notificarAdhesion = (adhesion) => {
    return new Promise((resolve, reject) => {
        const request = new AdhesionRequest();
        request.setIdevento(adhesion.idEvento);
        request.setIdorganizador(adhesion.idOrganizador);

        const voluntario = new Voluntario();
        voluntario.setIdorganizacion(adhesion.voluntario.idOrganizacion);
        voluntario.setIdvoluntario(adhesion.voluntario.idVoluntario);
        voluntario.setNombre(adhesion.voluntario.nombre);
        voluntario.setApellido(adhesion.voluntario.apellido);
        voluntario.setTelefono(adhesion.voluntario.telefono);
        voluntario.setEmail(adhesion.voluntario.email);

        request.setVoluntario(voluntario);

        client.notificarAdhesion(request, {}, (err, response) => {
            if (err) {
                console.error("Error al notificar adhesión:", err);
                return reject(err);
            }
            resolve(response.getMensaje());
        });
    });
};