package org.example.ProyectoGrpc.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.example.ProyectoGrpc.grpc.EventoSolidarioServicioRpc;
import org.example.ProyectoGrpc.grpc.InventarioDonacionesServicioRpc;
import org.example.ProyectoGrpc.grpc.UsuarioServicioRpc;
import org.example.ProyectoGrpc.servicio.EventoSolidarioServicio;
import org.example.ProyectoGrpc.servicio.InventarioDonacionesServicio;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GrpcServerStarter {

    private final UsuarioServicio usuarioServicio;
    private final EventoSolidarioServicio eventoServicio;
    private final InventarioDonacionesServicio inventarioServicio;

    public GrpcServerStarter(UsuarioServicio usuarioServicio,
                             EventoSolidarioServicio eventoServicio,
                             InventarioDonacionesServicio inventarioServicio) {
        this.usuarioServicio = usuarioServicio;
        this.eventoServicio = eventoServicio;
        this.inventarioServicio = inventarioServicio;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initGrpcServer() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090)
                .addService(new UsuarioServicioRpc(usuarioServicio))
                .addService(new EventoSolidarioServicioRpc(eventoServicio, usuarioServicio))
                .addService(new InventarioDonacionesServicioRpc(inventarioServicio))
                .build()
                .start();

        System.out.println("Servidor gRPC iniciado en el puerto 9090...");
        server.awaitTermination();
    }
}