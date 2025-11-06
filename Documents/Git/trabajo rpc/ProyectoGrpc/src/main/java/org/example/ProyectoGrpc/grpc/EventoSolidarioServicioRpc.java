package org.example.ProyectoGrpc.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.ProyectoGrpc.entidad.EventoSolidario;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.servicio.EventoSolidarioServicio;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import com.google.protobuf.Timestamp;

import java.time.LocalDateTime;

@GrpcService
public class EventoSolidarioServicioRpc extends EventoSolidarioServiceGrpc.EventoSolidarioServiceImplBase {

    private final EventoSolidarioServicio eventoServicio;

    private final UsuarioServicio usuarioServicio;

    public EventoSolidarioServicioRpc(EventoSolidarioServicio eventoServicio, UsuarioServicio usuarioServicio) {
        this.eventoServicio = eventoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @Override
    public void altaEvento(UsuarioOuterClass.EventoSolidario request,
            StreamObserver<UsuarioOuterClass.EventoSolidario> responseObserver) {

        EventoSolidario evento = new EventoSolidario();
        evento.setNombreEvento(request.getNombreEvento());
        evento.setDescripcion(request.getDescripcion());
        evento.setFechaHoraEvento(timestampToLocalDateTime(request.getFechaHoraEvento()));

        EventoSolidario creado = eventoServicio.altaEvento(evento);

        UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(creado.getId())
                .setNombreEvento(creado.getNombreEvento())
                .setDescripcion(creado.getDescripcion())
                .setFechaHoraEvento(localDateTimeToTimestamp(creado.getFechaHoraEvento()))
                .build();

        responseObserver.onNext(grpcEvento);
        responseObserver.onCompleted();
    }

    @Override
    public void modificarEvento(UsuarioOuterClass.EventoSolidario request,
            StreamObserver<UsuarioOuterClass.EventoSolidario> responseObserver) {

        LocalDateTime fecha = timestampToLocalDateTime(request.getFechaHoraEvento());

        Set<Usuario> participantes = new HashSet<>();
        for (UsuarioOuterClass.Usuario u : request.getParticipantesEventoList()) {
            Usuario usuario = usuarioServicio.buscarPorId(u.getId());
            if (usuario != null)
                participantes.add(usuario);
        }

        EventoSolidario evento = new EventoSolidario();
        evento.setNombreEvento(request.getNombreEvento());
        evento.setDescripcion(request.getDescripcion());
        evento.setFechaHoraEvento(fecha);
        evento.setParticipantesEvento(participantes);

        EventoSolidario modificado = eventoServicio.modificarEvento(request.getId(), evento);

        UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(modificado.getId())
                .setNombreEvento(modificado.getNombreEvento())
                .setDescripcion(modificado.getDescripcion())
                .setFechaHoraEvento(localDateTimeToTimestamp(modificado.getFechaHoraEvento()))
                .build();

        responseObserver.onNext(grpcEvento);
        responseObserver.onCompleted();
    }

    @Override
    public void bajaEvento(UsuarioOuterClass.EventoIdRequest request,
            StreamObserver<com.google.protobuf.Empty> responseObserver) {

        eventoServicio.bajaEvento(request.getId());
        responseObserver.onNext(com.google.protobuf.Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void buscarEventoPorId(UsuarioOuterClass.EventoIdRequest request,
            StreamObserver<UsuarioOuterClass.EventoSolidario> responseObserver) {

        EventoSolidario evento = eventoServicio.buscarPorId(request.getId());
        if (evento != null) {
            UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                    .setId(evento.getId())
                    .setNombreEvento(evento.getNombreEvento())
                    .setDescripcion(evento.getDescripcion())
                    .setFechaHoraEvento(localDateTimeToTimestamp(evento.getFechaHoraEvento()))
                    .build();
            responseObserver.onNext(grpcEvento);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void listarEventos(Empty request,
            StreamObserver<UsuarioOuterClass.EventoListResponse> responseObserver) {

        List<EventoSolidario> eventos = eventoServicio.listarTodos();
        UsuarioOuterClass.EventoListResponse.Builder responseBuilder = UsuarioOuterClass.EventoListResponse
                .newBuilder();

        for (EventoSolidario evento : eventos) {
            UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                    .setId(evento.getId())
                    .setNombreEvento(evento.getNombreEvento())
                    .setDescripcion(evento.getDescripcion())
                    .setFechaHoraEvento(localDateTimeToTimestamp(evento.getFechaHoraEvento()))
                    .addAllParticipantesEvento(
                            evento.getParticipantesEvento().stream()
                                    .map(this::mapUsuarioAGrpc)
                                    .collect(Collectors.toList()))
                    .build();
            responseBuilder.addEventos(grpcEvento);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private UsuarioOuterClass.Usuario mapUsuarioAGrpc(Usuario u) {
        return UsuarioOuterClass.Usuario.newBuilder()
                .setId(u.getId())
                .setNombreUsuario(u.getNombreUsuario())
                .setNombre(u.getNombre())
                .setApellido(u.getApellido())
                .setTelefono(u.getTelefono() != null ? u.getTelefono() : "")
                .setClave(u.getPassword())
                .setEmail(u.getEmail())
                .setRol(u.getRol().name())
                .setActivo(u.isActivo())
                .build();
    }

    @Override
    public void asignarParticipantes(UsuarioOuterClass.EventoParticipantesRequest request,
            StreamObserver<UsuarioOuterClass.EventoSolidario> responseObserver) {

        String rolSolicitante = request.getRolSolicitante();

        for (long usuarioId : request.getUsuarioIdsList()) {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);
            if (usuario != null) {
                eventoServicio.agregarMiembro(request.getEventoId(), usuario, rolSolicitante);
            }
        }

        EventoSolidario actualizado = eventoServicio.buscarPorId(request.getEventoId());
        if (actualizado == null) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Evento no encontrado")
                    .asRuntimeException());
            return;
        }

        UsuarioOuterClass.EventoSolidario.Builder grpcEventoBuilder = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(actualizado.getId())
                .setNombreEvento(actualizado.getNombreEvento())
                .setDescripcion(actualizado.getDescripcion())
                .setFechaHoraEvento(localDateTimeToTimestamp(actualizado.getFechaHoraEvento()));

        grpcEventoBuilder.addAllParticipantesEvento(
                actualizado.getParticipantesEvento().stream()
                        .map(this::mapUsuarioAGrpc)
                        .collect(Collectors.toList()));

        responseObserver.onNext(grpcEventoBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void quitarParticipantes(UsuarioOuterClass.EventoParticipantesRequest request,
            StreamObserver<UsuarioOuterClass.EventoSolidario> responseObserver) {

        String rolSolicitante = request.getRolSolicitante();

        for (long usuarioId : request.getUsuarioIdsList()) {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);
            if (usuario != null) {
                eventoServicio.quitarMiembro(request.getEventoId(), usuario, rolSolicitante);
            }
        }

        EventoSolidario actualizado = eventoServicio.buscarPorId(request.getEventoId());
        if (actualizado == null) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Evento no encontrado")
                    .asRuntimeException());
            return;
        }

        UsuarioOuterClass.EventoSolidario.Builder grpcEventoBuilder = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(actualizado.getId())
                .setNombreEvento(actualizado.getNombreEvento())
                .setDescripcion(actualizado.getDescripcion())
                .setFechaHoraEvento(localDateTimeToTimestamp(actualizado.getFechaHoraEvento()));

        grpcEventoBuilder.addAllParticipantesEvento(
                actualizado.getParticipantesEvento().stream()
                        .map(this::mapUsuarioAGrpc)
                        .collect(Collectors.toList()));

        responseObserver.onNext(grpcEventoBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void registrarDonacionEvento(UsuarioOuterClass.DonacionEventoRequest request,
            StreamObserver<com.google.protobuf.Empty> responseObserver) {
        try {
            eventoServicio.registrarDonacionEvento(
                    request.getEventoId(),
                    request.getInventarioId(),
                    request.getCantidad(),
                    request.getUsuarioId());
            responseObserver.onNext(com.google.protobuf.Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Error registrando donación: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    // --- Helpers para convertir Timestamp <-> LocalDateTime ---
    private LocalDateTime timestampToLocalDateTime(Timestamp ts) {
        Instant instant = Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private Timestamp localDateTimeToTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return Timestamp.newBuilder().setSeconds(0).build(); // evita nulos
        }
        return Timestamp.newBuilder()
                .setSeconds(dateTime.toEpochSecond(ZoneOffset.UTC))
                .setNanos(dateTime.getNano())
                .build();
    }
}