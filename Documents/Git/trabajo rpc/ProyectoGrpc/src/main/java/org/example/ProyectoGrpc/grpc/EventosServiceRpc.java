package org.example.ProyectoGrpc.grpc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.myorg.kafka_module.dto.AdhesionDTO;
import com.myorg.kafka_module.dto.EventoDTO;
import com.myorg.kafka_module.dto.VoluntarioDTO;
import com.myorg.kafka_module.service.AdhesionService;
import com.myorg.kafka_module.service.EventoService;

import donaciones.Donaciones.BajaEventoRequest;
import donaciones.Donaciones.EventoRequest;
import donaciones.Donaciones.ListaEventosResponse;
import donaciones.Donaciones.Respuesta;
import donaciones.Donaciones.Vacio;
import donaciones.EventosServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class EventosServiceRpc extends EventosServiceGrpc.EventosServiceImplBase {
    @Autowired
    private EventoService eventoService;

    @Autowired
    private AdhesionService adhesionService;

    public EventosServiceRpc(EventoService eventoService, AdhesionService adhesionService) {
        this.eventoService = eventoService;
        this.adhesionService = adhesionService;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    ZoneId zonaArg = ZoneId.of("America/Argentina/Buenos_Aires");

    @Override
    public void publicarEvento(EventoRequest request, StreamObserver<Respuesta> responseObserver) {

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(request.getFechaHora(), formatter);
        ZonedDateTime fechaHora = offsetDateTime.atZoneSameInstant(zonaArg);

        
        EventoDTO dto = new EventoDTO();
        dto.setIdOrganizacion(request.getIdOrganizacion());
        dto.setIdEvento(request.getIdEvento());
        dto.setNombreEvento(request.getNombre());
        dto.setDescripcion(request.getDescripcion());
        dto.setFechaHora(fechaHora.toLocalDateTime());

        eventoService.publicarEvento(dto);

        responseObserver.onNext(Respuesta.newBuilder().setMensaje("Evento publicado").build());
        responseObserver.onCompleted();
    }

    @Override
    public void listarEventosExternos(Vacio request, StreamObserver<ListaEventosResponse> responseObserver) {
        try {
            var eventos = eventoService.obtenerEventosExternos();
            ListaEventosResponse.Builder response = ListaEventosResponse.newBuilder();

            for (EventoDTO e : eventos) {
                response.addEventos(
                        EventoRequest.newBuilder()
                                .setIdOrganizacion(e.getIdOrganizacion())
                                .setIdEvento(e.getIdEvento())
                                .setNombre(e.getNombreEvento())
                                .setDescripcion(e.getDescripcion())
                                .setFechaHora(
                                        e.getFechaHora() != null
                                                ? e.getFechaHora().atZone(zonaArg).format(formatter2)
                                                : "")
                                .build());
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error al listar eventos externos: " + e.getMessage())
                            .asRuntimeException());
        }
    }

    @Override
    public void listarEventosPropios(Vacio request, StreamObserver<ListaEventosResponse> responseObserver) {
        try {
            var eventos = eventoService.obtenerEventosPropios();
            ListaEventosResponse.Builder response = ListaEventosResponse.newBuilder();

            for (EventoDTO e : eventos) {
                response.addEventos(
                        EventoRequest.newBuilder()
                                .setIdOrganizacion(e.getIdOrganizacion())
                                .setIdEvento(e.getIdEvento())
                                .setNombre(e.getNombreEvento())
                                .setDescripcion(e.getDescripcion())
                                .setFechaHora(
                                        e.getFechaHora() != null
                                                ? e.getFechaHora().atZone(zonaArg).format(formatter2)
                                                : "")
                                .build());
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error al listar eventos propios: " + e.getMessage())
                            .asRuntimeException());
        }
    }

    @Override
    public void bajaEvento(BajaEventoRequest request, StreamObserver<Respuesta> responseObserver) {
        try {
            System.out.println("Baja recibida de evento: " + request.getIdEvento());
            eventoService.darDeBajaEvento(request.getIdEvento());

            responseObserver.onNext(
                    Respuesta.newBuilder()
                            .setMensaje("Evento dado de baja correctamente")
                            .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error al dar de baja evento: " + e.getMessage())
                            .asRuntimeException());
        }
    }

    @Override
    public void notificarAdhesion(donaciones.Donaciones.AdhesionRequest request,
            StreamObserver<donaciones.Donaciones.Respuesta> responseObserver) {
        try {
            var adhesion = new AdhesionDTO();
            adhesion.setIdEvento(request.getIdEvento());
            adhesion.setIdOrganizador(request.getIdOrganizador());

            var voluntario = new VoluntarioDTO();
            voluntario.setIdOrganizacion(request.getVoluntario().getIdOrganizacion());
            voluntario.setIdVoluntario(request.getVoluntario().getIdVoluntario());
            voluntario.setNombre(request.getVoluntario().getNombre());
            voluntario.setApellido(request.getVoluntario().getApellido());
            voluntario.setTelefono(request.getVoluntario().getTelefono());
            voluntario.setEmail(request.getVoluntario().getEmail());

            adhesion.setVoluntario(voluntario);

            adhesionService.notificarAdhesion(adhesion);

            responseObserver.onNext(
                    donaciones.Donaciones.Respuesta.newBuilder()
                            .setMensaje("Adhesión enviada correctamente")
                            .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error al notificar adhesión: " + e.getMessage())
                            .asRuntimeException());
        }
    }

}
