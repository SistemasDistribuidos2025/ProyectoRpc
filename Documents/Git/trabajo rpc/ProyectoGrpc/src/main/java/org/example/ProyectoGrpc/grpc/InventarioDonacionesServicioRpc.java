package org.example.ProyectoGrpc.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.ProyectoGrpc.entidad.InventarioDonaciones;
import org.example.ProyectoGrpc.enums.CategoriaDonacion;
import org.example.ProyectoGrpc.servicio.InventarioDonacionesServicio;
import org.springframework.stereotype.Component;
import com.google.protobuf.Empty;
import java.time.format.DateTimeFormatter;
import java.util.List;


@GrpcService
@Component // Spring va a manejar la instancia
public class InventarioDonacionesServicioRpc extends InventarioDonacionesServiceGrpc.InventarioDonacionesServiceImplBase {

    private final InventarioDonacionesServicio inventarioServicio;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public InventarioDonacionesServicioRpc(InventarioDonacionesServicio inventarioServicio) {
        this.inventarioServicio = inventarioServicio;
    }

    @Override
    public void altaInventario(UsuarioOuterClass.InventarioDonaciones request,
                               StreamObserver<UsuarioOuterClass.InventarioDonaciones> responseObserver) {

        try {
            InventarioDonaciones inventario = new InventarioDonaciones();
            inventario.setCategoria(CategoriaDonacion.valueOf(request.getCategoria().name()));
            inventario.setDescripcion(request.getDescripcion());
            inventario.setCantidad(request.getCantidad());

            InventarioDonaciones creado = inventarioServicio.altaInventario(inventario, request.getUsuarioAltaId());

            UsuarioOuterClass.InventarioDonaciones grpcInventario = UsuarioOuterClass.InventarioDonaciones.newBuilder()
                    .setId(creado.getId())
                    .setCategoria(request.getCategoria())
                    .setDescripcion(creado.getDescripcion())
                    .setCantidad(creado.getCantidad())
                    .setEliminado(creado.isEliminado())
                    .setUsuarioAltaId(creado.getUsuarioAlta().getId())
                    .setFechaHoraAlta(creado.getFechaHoraAlta().format(formatter))
                    .build();

            responseObserver.onNext(grpcInventario);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Error al crear inventario")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void modificarInventario(UsuarioOuterClass.InventarioDonaciones request,
                                    StreamObserver<UsuarioOuterClass.InventarioDonaciones> responseObserver) {

        try {
            InventarioDonaciones modificado = inventarioServicio.modificarInventario(
                    request.getId(),
                    request.getDescripcion(),
                    request.getCantidad(),
                    request.getUsuarioModificadoId()
            );

            UsuarioOuterClass.InventarioDonaciones grpcInventario = UsuarioOuterClass.InventarioDonaciones.newBuilder()
                    .setId(modificado.getId())
                    .setCategoria(UsuarioOuterClass.CategoriaDonacion.valueOf(modificado.getCategoria().name()))
                    .setDescripcion(modificado.getDescripcion())
                    .setCantidad(modificado.getCantidad())
                    .setEliminado(modificado.isEliminado())
                    .setUsuarioAltaId(modificado.getUsuarioAlta().getId())
                    .setUsuarioModificadoId(modificado.getUsuarioModificado() != null ? modificado.getUsuarioModificado().getId() : 0)
                    .setFechaHoraAlta(modificado.getFechaHoraAlta().format(formatter))
                    .setFechaHoraModificacion(modificado.getFechaHoraModificacion() != null ? modificado.getFechaHoraModificacion().format(formatter) : "")
                    .build();

            responseObserver.onNext(grpcInventario);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Error al modificar inventario")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void bajaInventario(UsuarioOuterClass.InventarioIdRequest request,
                               StreamObserver<Empty> responseObserver) {
        try {
            Long usuarioModificacionId = request.getUsuarioModificadoId();
            inventarioServicio.bajaInventario(request.getId(), usuarioModificacionId);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Error al eliminar inventario")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void buscarPorId(UsuarioOuterClass.InventarioIdRequest request,
                            StreamObserver<UsuarioOuterClass.InventarioDonaciones> responseObserver) {

        try {
            InventarioDonaciones inventario = inventarioServicio.buscarPorId(request.getId());
            if (inventario != null) {
                UsuarioOuterClass.InventarioDonaciones grpcInventario = UsuarioOuterClass.InventarioDonaciones.newBuilder()
                        .setId(inventario.getId())
                        .setCategoria(UsuarioOuterClass.CategoriaDonacion.valueOf(inventario.getCategoria().name()))
                        .setDescripcion(inventario.getDescripcion())
                        .setCantidad(inventario.getCantidad())
                        .setEliminado(inventario.isEliminado())
                        .setUsuarioAltaId(inventario.getUsuarioAlta().getId())
                        .setUsuarioModificadoId(inventario.getUsuarioModificado() != null ? inventario.getUsuarioModificado().getId() : 0)
                        .setFechaHoraAlta(inventario.getFechaHoraAlta().format(formatter))
                        .setFechaHoraModificacion(inventario.getFechaHoraModificacion() != null ? inventario.getFechaHoraModificacion().format(formatter) : "")
                        .build();
                responseObserver.onNext(grpcInventario);
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Error al buscar inventario")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void listarTodos(Empty request,
                            StreamObserver<UsuarioOuterClass.InventarioListResponse> responseObserver) {

        try {
            List<InventarioDonaciones> inventarios = inventarioServicio.listarTodos();
            UsuarioOuterClass.InventarioListResponse.Builder responseBuilder = UsuarioOuterClass.InventarioListResponse.newBuilder();

            for (InventarioDonaciones inventario : inventarios) {
                UsuarioOuterClass.InventarioDonaciones grpcInventario = UsuarioOuterClass.InventarioDonaciones.newBuilder()
                        .setId(inventario.getId())
                        .setCategoria(UsuarioOuterClass.CategoriaDonacion.valueOf(inventario.getCategoria().name()))
                        .setDescripcion(inventario.getDescripcion())
                        .setCantidad(inventario.getCantidad())
                        .setEliminado(inventario.isEliminado())
                        .setUsuarioAltaId(inventario.getUsuarioAlta().getId())
                        .setUsuarioModificadoId(inventario.getUsuarioModificado() != null ? inventario.getUsuarioModificado().getId() : 0)
                        .setFechaHoraAlta(inventario.getFechaHoraAlta().format(formatter))
                        .setFechaHoraModificacion(inventario.getFechaHoraModificacion() != null ? inventario.getFechaHoraModificacion().format(formatter) : "")
                        .build();
                responseBuilder.addInventarios(grpcInventario);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Error al listar inventarios")
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
