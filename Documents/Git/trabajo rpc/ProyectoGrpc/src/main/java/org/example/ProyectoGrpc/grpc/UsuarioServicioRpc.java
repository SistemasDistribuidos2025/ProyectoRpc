package org.example.ProyectoGrpc.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;

import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.enums.RolUsuario;
import org.example.ProyectoGrpc.grpc.UsuarioOuterClass;
import org.example.ProyectoGrpc.grpc.UsuarioServiceGrpc;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;

import java.util.List;

public class UsuarioServicioRpc extends UsuarioServiceGrpc.UsuarioServiceImplBase {

    private final UsuarioServicio usuarioServicio;

    public UsuarioServicioRpc(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @Override
    public void altaUsuario(UsuarioOuterClass.Usuario request, StreamObserver<UsuarioOuterClass.UsuarioResponse> responseObserver) {
        // Convertir de gRPC Usuario a entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        usuario.setEmail(request.getEmail());
        usuario.setRol(RolUsuario.valueOf(request.getRol())); 

        // Llamar al servicio
        Usuario usuarioCreado = usuarioServicio.altaUsuario(usuario);

        // Convertir de entidad a gRPC
        UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                .setId(usuarioCreado.getId())
                .setNombreUsuario(usuarioCreado.getNombreUsuario())
                .setNombre(usuarioCreado.getNombre())
                .setApellido(usuarioCreado.getApellido())
                .setTelefono(usuarioCreado.getTelefono() != null ? usuarioCreado.getTelefono() : "")
                .setClave(usuarioCreado.getPassword())
                .setEmail(usuarioCreado.getEmail())
                .setRol(usuarioCreado.getRol().name())
                .setActivo(usuarioCreado.isActivo())
                .build();

        UsuarioOuterClass.UsuarioResponse response = UsuarioOuterClass.UsuarioResponse.newBuilder()
                .setUsuario(grpcUsuario)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void modificarUsuario(UsuarioOuterClass.Usuario request, StreamObserver<UsuarioOuterClass.UsuarioResponse> responseObserver) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        usuario.setEmail(request.getEmail());
        usuario.setRol(RolUsuario.valueOf(request.getRol()));
        usuario.setActivo(request.getActivo());

        Usuario usuarioModificado = usuarioServicio.modificarUsuario(request.getId(), usuario);

        UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                .setId(usuarioModificado.getId())
                .setNombreUsuario(usuarioModificado.getNombreUsuario())
                .setNombre(usuarioModificado.getNombre())
                .setApellido(usuarioModificado.getApellido())
                .setTelefono(usuarioModificado.getTelefono() != null ? usuarioModificado.getTelefono() : "")
                .setClave(usuarioModificado.getPassword())
                .setEmail(usuarioModificado.getEmail())
                .setRol(usuarioModificado.getRol().name())
                .setActivo(usuarioModificado.isActivo())
                .build();

        UsuarioOuterClass.UsuarioResponse response = UsuarioOuterClass.UsuarioResponse.newBuilder()
                .setUsuario(grpcUsuario)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void bajaUsuario(UsuarioOuterClass.UsuarioIdRequest request, StreamObserver<UsuarioOuterClass.UsuarioResponse> responseObserver) {
    	Usuario usuario = usuarioServicio.bajaUsuario(request.getId());

        UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                .setId(usuario.getId())
                .setNombre(usuario.getNombre())
                .setEmail(usuario.getEmail())
                .setActivo(usuario.isActivo())
                .build();

        UsuarioOuterClass.UsuarioResponse response = UsuarioOuterClass.UsuarioResponse.newBuilder()
                .setUsuario(grpcUsuario)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void buscarPorId(UsuarioOuterClass.UsuarioIdRequest request, StreamObserver<UsuarioOuterClass.UsuarioResponse> responseObserver) {
        Usuario usuario = usuarioServicio.buscarPorId(request.getId());

        if (usuario != null) {
            UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                    .setId(usuario.getId())
                    .setNombreUsuario(usuario.getNombreUsuario())
                    .setNombre(usuario.getNombre())
                    .setApellido(usuario.getApellido())
                    .setTelefono(usuario.getTelefono() != null ? usuario.getTelefono() : "")
                    .setClave(usuario.getPassword())
                    .setEmail(usuario.getEmail())
                    .setRol(usuario.getRol().name())
                    .setActivo(usuario.isActivo())
                    .build();

            UsuarioOuterClass.UsuarioResponse response = UsuarioOuterClass.UsuarioResponse.newBuilder()
                    .setUsuario(grpcUsuario)
                    .build();

            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void listarTodos(Empty request, StreamObserver<UsuarioOuterClass.UsuarioListResponse> responseObserver) {
        List<Usuario> usuarios = usuarioServicio.listarTodos();

        UsuarioOuterClass.UsuarioListResponse.Builder responseBuilder = UsuarioOuterClass.UsuarioListResponse.newBuilder();
        for (Usuario usuario : usuarios) {
            UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                    .setId(usuario.getId())
                    .setNombreUsuario(usuario.getNombreUsuario())
                    .setNombre(usuario.getNombre())
                    .setApellido(usuario.getApellido())
                    .setTelefono(usuario.getTelefono() != null ? usuario.getTelefono() : "")
                    .setClave(usuario.getPassword())
                    .setEmail(usuario.getEmail())
                    .setRol(usuario.getRol().name())
                    .setActivo(usuario.isActivo())
                    .build();
            responseBuilder.addUsuarios(grpcUsuario);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void buscarPorEmail(UsuarioOuterClass.UsuarioEmailRequest request,
                               StreamObserver<UsuarioOuterClass.UsuarioResponse> responseObserver) {
        Usuario usuario = usuarioServicio.buscarPorEmail(request.getEmail());

        if (usuario != null) {
            UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                    .setId(usuario.getId())
                    .setNombreUsuario(usuario.getNombreUsuario())
                    .setNombre(usuario.getNombre())
                    .setApellido(usuario.getApellido())
                    .setTelefono(usuario.getTelefono() != null ? usuario.getTelefono() : "")
                    .setClave(usuario.getPassword())
                    .setEmail(usuario.getEmail())
                    .setRol(usuario.getRol().name())
                    .setActivo(usuario.isActivo())
                    .build();

            UsuarioOuterClass.UsuarioResponse response = UsuarioOuterClass.UsuarioResponse.newBuilder()
                    .setUsuario(grpcUsuario)
                    .build();

            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void buscarPorNombreUsuario(UsuarioOuterClass.UsuarioNombreRequest request,
                                       StreamObserver<UsuarioOuterClass.UsuarioResponse> responseObserver) {
        Usuario usuario = usuarioServicio.buscarPorNombreUsuario(request.getNombreUsuario());

        if (usuario != null) {
            UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                    .setId(usuario.getId())
                    .setNombreUsuario(usuario.getNombreUsuario())
                    .setNombre(usuario.getNombre())
                    .setApellido(usuario.getApellido())
                    .setTelefono(usuario.getTelefono() != null ? usuario.getTelefono() : "")
                    .setClave(usuario.getPassword())
                    .setEmail(usuario.getEmail())
                    .setRol(usuario.getRol().name())
                    .setActivo(usuario.isActivo())
                    .build();

            UsuarioOuterClass.UsuarioResponse response = UsuarioOuterClass.UsuarioResponse.newBuilder()
                    .setUsuario(grpcUsuario)
                    .build();

            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void login(UsuarioOuterClass.LoginRequest request, StreamObserver<UsuarioOuterClass.LoginResponse> responseObserver) {
    	
    	
        try {
        	
            Usuario usuario = usuarioServicio.login(request.getIdentificador(), request.getPassword());

            UsuarioOuterClass.Usuario grpcUsuario = UsuarioOuterClass.Usuario.newBuilder()
                    .setId(usuario.getId())
                    .setNombreUsuario(usuario.getNombreUsuario())
                    .setNombre(usuario.getNombre())
                    .setApellido(usuario.getApellido())
                    .setTelefono(usuario.getTelefono() != null ? usuario.getTelefono() : "")
                    .setClave(usuario.getPassword())
                    .setEmail(usuario.getEmail())
                    .setRol(usuario.getRol().name())
                    .setActivo(usuario.isActivo())
                    .build();

            UsuarioOuterClass.LoginResponse response = UsuarioOuterClass.LoginResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Login exitoso")
                    .setUsuario(grpcUsuario)
                    .build();

            responseObserver.onNext(response);
        } catch (RuntimeException e) {
            UsuarioOuterClass.LoginResponse response = UsuarioOuterClass.LoginResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .build();
            responseObserver.onNext(response);
        } finally {
            responseObserver.onCompleted();
        }
    }
    }