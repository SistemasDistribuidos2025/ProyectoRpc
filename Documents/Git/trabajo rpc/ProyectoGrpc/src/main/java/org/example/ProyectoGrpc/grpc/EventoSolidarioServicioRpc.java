package org.example.ProyectoGrpc.grpc;

import io.grpc.stub.StreamObserver;
import org.example.ProyectoGrpc.entidad.EventoSolidario;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.grpc.EventoSolidarioServiceGrpc;
import org.example.ProyectoGrpc.grpc.UsuarioOuterClass;
import org.example.ProyectoGrpc.servicio.EventoSolidarioServicio;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventoSolidarioServicioRpc extends EventoSolidarioServiceGrpc.EventoSolidarioServiceImplBase {

    private final EventoSolidarioServicio eventoServicio;

    private final UsuarioServicio usuarioServicio;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        evento.setFechaHoraEvento(java.time.LocalDateTime.parse(request.getFechaHoraEvento(), formatter));

        EventoSolidario creado = eventoServicio.altaEvento(evento);

        UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(creado.getId())
                .setNombreEvento(creado.getNombreEvento())
                .setDescripcion(creado.getDescripcion())
                .setFechaHoraEvento(creado.getFechaHoraEvento().format(formatter))
                
                .build();

        responseObserver.onNext(grpcEvento);
        responseObserver.onCompleted();
    }

    @Override
    public void modificarEvento(UsuarioOuterClass.EventoSolidario request,
                                StreamObserver<UsuarioOuterClass.EventoSolidario> responseObserver) {

        // Convertimos la fecha de String a LocalDateTime
        LocalDateTime fecha = java.time.LocalDateTime.parse(request.getFechaHoraEvento(), formatter);

        // Convertimos la lista de participantes de UsuarioOuterClass a Usuario
        List<Usuario> participantes = new ArrayList<>();
        for (UsuarioOuterClass.Usuario u : request.getParticipantesEventoList()) {
            Usuario usuario = usuarioServicio.buscarPorId(u.getId());
            if (usuario != null) participantes.add(usuario);
        }

        // Creamos un objeto EventoSolidario "temporal" con los datos del request
        EventoSolidario evento = new EventoSolidario();
        evento.setNombreEvento(request.getNombreEvento());
        evento.setDescripcion(request.getDescripcion());
        evento.setFechaHoraEvento(fecha);
        evento.setParticipantesEvento(participantes);

        // Llamamos al servicio
        EventoSolidario modificado = eventoServicio.modificarEvento(request.getId(), evento);

        // Convertimos a objeto gRPC para la respuesta
        UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(modificado.getId())
                .setNombreEvento(modificado.getNombreEvento())
                .setDescripcion(modificado.getDescripcion())
                .setFechaHoraEvento(modificado.getFechaHoraEvento().format(formatter))
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
                    .setFechaHoraEvento(evento.getFechaHoraEvento().format(formatter))
                    .build();
            responseObserver.onNext(grpcEvento);
        }
        responseObserver.onCompleted();
    }

    //mapeo los datos del usuario para poder agregar la lista de participantes a cada evento
    private UsuarioOuterClass.Usuario mapUsuarioAGrpc (Usuario u) {
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
    public void listarEventos(com.google.protobuf.Empty request,
                              StreamObserver<UsuarioOuterClass.EventoListResponse> responseObserver) {

        List<EventoSolidario> eventos = eventoServicio.listarTodos();
        UsuarioOuterClass.EventoListResponse.Builder responseBuilder = UsuarioOuterClass.EventoListResponse.newBuilder();

        for (EventoSolidario evento : eventos) {
            UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                    .setId(evento.getId())
                    .setNombreEvento(evento.getNombreEvento())
                    .setDescripcion(evento.getDescripcion())
                    .setFechaHoraEvento(evento.getFechaHoraEvento().format(formatter))
                    .addAllParticipantesEvento(
                            evento.getParticipantesEvento().stream()
                                  .map(this::mapUsuarioAGrpc)
                                  .collect(Collectors.toList())
                                  )
                    .build();
            responseBuilder.addEventos(grpcEvento);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void asignarParticipantes(UsuarioOuterClass.EventoParticipantesRequest request,
                                     StreamObserver<UsuarioOuterClass.EventoSolidario> responseObserver) {

        String rolSolicitante = request.getRolSolicitante(); // asumimos que lo pasas en el request

        for (long usuarioId : request.getUsuarioIdsList()) {
            Usuario usuario = usuarioServicio.buscarPorId(usuarioId);
            if (usuario != null) {
                eventoServicio.agregarMiembro(request.getEventoId(), usuario, rolSolicitante);
            }
        }

        EventoSolidario actualizado = eventoServicio.buscarPorId(request.getEventoId());

        UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(actualizado.getId())
                .setNombreEvento(actualizado.getNombreEvento())
                .setDescripcion(actualizado.getDescripcion())
                .setFechaHoraEvento(actualizado.getFechaHoraEvento().format(formatter))
                .build();

        responseObserver.onNext(grpcEvento);
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

        UsuarioOuterClass.EventoSolidario grpcEvento = UsuarioOuterClass.EventoSolidario.newBuilder()
                .setId(actualizado.getId())
                .setNombreEvento(actualizado.getNombreEvento())
                .setDescripcion(actualizado.getDescripcion())
                .setFechaHoraEvento(actualizado.getFechaHoraEvento().format(formatter))
                .build();

        responseObserver.onNext(grpcEvento);
        responseObserver.onCompleted();
    }
}
