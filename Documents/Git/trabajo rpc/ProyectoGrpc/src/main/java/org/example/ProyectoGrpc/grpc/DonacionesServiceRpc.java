package org.example.ProyectoGrpc.grpc;

import donaciones.DonacionesServiceGrpc;
import donaciones.Donaciones.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.myorg.kafka_module.dto.SolicitudDonacionDTO;
import com.myorg.kafka_module.dto.OfertaDonacionDTO;
import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import org.example.ProyectoGrpc.servicio.implementacion.DonacionesService;

import io.grpc.stub.StreamObserver;
import java.util.stream.Collectors;

@GrpcService
public class DonacionesServiceRpc extends DonacionesServiceGrpc.DonacionesServiceImplBase {

    @Autowired
    private DonacionesService donacionesService;

    public DonacionesServiceRpc(DonacionesService donacionesService) {
        this.donacionesService = donacionesService;
    }

    // -------------------- SOLICITUDES --------------------
    @Override
    public void enviarSolicitud(SolicitudDonacionRequest request, StreamObserver<Respuesta> responseObserver) {
        donacionesService.solicitarDonaciones(new SolicitudDonacionDTO(
                request.getIdOrganizacion(),
                request.getIdSolicitud(),
                request.getDonacionesList().stream()
                        .map(item -> new com.myorg.kafka_module.dto.ItemDonacionDTO(item.getCategoria(), item.getDescripcion()))
                        .collect(Collectors.toList())
        ));

        Respuesta respuesta = Respuesta.newBuilder()
                .setMensaje("✅ Solicitud enviada correctamente")
                .build();
        responseObserver.onNext(respuesta);
        responseObserver.onCompleted();
    }

    @Override
    public void listarSolicitudes(Vacio request, StreamObserver<ListaSolicitudesResponse> responseObserver) {
        ListaSolicitudesResponse.Builder builder = ListaSolicitudesResponse.newBuilder();

        donacionesService.obtenerSolicitudesRecibidas().forEach(solicitud -> {
            SolicitudDonacionRequest.Builder solicitudProto = SolicitudDonacionRequest.newBuilder()
                            .setIdOrganizacion(solicitud.getIdOrganizacion())
                            .setIdSolicitud(solicitud.getIdSolicitud());

            solicitud.getDonaciones().forEach(item ->
                    solicitudProto.addDonaciones(
                            ItemDonacion.newBuilder()
                                    .setCategoria(item.getCategoria())
                                    .setDescripcion(item.getDescripcion())
                                    .build()
                    )
            );

            builder.addSolicitudes(solicitudProto.build());
        });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    // -------------------- OFERTAS --------------------
    @Override
    public void enviarOferta(OfertaDonacionRequest request, StreamObserver<Respuesta> responseObserver) {
        donacionesService.ofrecerDonaciones(new OfertaDonacionDTO(
                request.getIdOferta(),
                request.getIdOrganizacionDonante(),
                request.getDonacionesList().stream()
                        .map(item -> new com.myorg.kafka_module.dto.ItemDonacionDTO(item.getCategoria(), item.getDescripcion()))
                        .collect(Collectors.toList())
        ));

        Respuesta respuesta = Respuesta.newBuilder()
                .setMensaje("✅ Oferta enviada correctamente")
                .build();
        responseObserver.onNext(respuesta);
        responseObserver.onCompleted();
    }

    @Override
    public void listarOfertas(Vacio request, StreamObserver<ListaOfertasResponse> responseObserver) {
        ListaOfertasResponse.Builder builder = ListaOfertasResponse.newBuilder();

        donacionesService.obtenerOfertasRecibidas().forEach(oferta -> {
            OfertaDonacionRequest.Builder ofertaProto = OfertaDonacionRequest.newBuilder()
                            .setIdOferta(oferta.getIdOferta())
                            .setIdOrganizacionDonante(oferta.getIdOrganizacionDonante());

            oferta.getDonaciones().forEach(item ->
                    ofertaProto.addDonaciones(
                            ItemDonacion.newBuilder()
                                    .setCategoria(item.getCategoria())
                                    .setDescripcion(item.getDescripcion())
                                    .build()
                    )
            );

            builder.addOfertas(ofertaProto.build());
        });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    // -------------------- TRANSFERENCIAS --------------------
    @Override
    public void enviarTransferencia(TransferenciaDonacionRequest request, StreamObserver<Respuesta> responseObserver) {
        donacionesService.transferirDonaciones(new TransferenciaDonacionDTO(
                request.getIdSolicitud(),
                request.getIdOrganizacionDonante(),
                request.getIdOrganizacionReceptora(),
                request.getDonacionesList().stream()
                        .map(item -> new com.myorg.kafka_module.dto.ItemDonacionDTO(item.getCategoria(), item.getDescripcion()))
                        .collect(Collectors.toList())
        ));

        Respuesta respuesta = Respuesta.newBuilder()
                .setMensaje("✅ Transferencia enviada correctamente")
                .build();
        responseObserver.onNext(respuesta);
        responseObserver.onCompleted();
    }
}