package org.example.ProyectoGrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.persistence.EntityManager;
import org.example.ProyectoGrpc.grpc.EventoSolidarioServicioRpc;
import org.example.ProyectoGrpc.grpc.InventarioDonacionesServicioRpc;
import org.example.ProyectoGrpc.grpc.UsuarioServicioRpc;
import org.example.ProyectoGrpc.repositorioDao.EventoSolidarioDao;
import org.example.ProyectoGrpc.repositorioDao.InventarioDonacionesDao;
import org.example.ProyectoGrpc.repositorioDao.UsuarioDao;
import org.example.ProyectoGrpc.repositorioDao.implementacionDao.EventoSolidarioDaoImp;
import org.example.ProyectoGrpc.repositorioDao.implementacionDao.InventarioDonacionesDaoImp;
import org.example.ProyectoGrpc.repositorioDao.implementacionDao.UsuarioDaoImp;
import org.example.ProyectoGrpc.servicio.EventoSolidarioServicio;
import org.example.ProyectoGrpc.servicio.InventarioDonacionesServicio;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;
import org.example.ProyectoGrpc.servicio.implementacion.EventoSolidarioServicioImp;
import org.example.ProyectoGrpc.servicio.implementacion.InventarioDonacionesServicioImp;
import org.example.ProyectoGrpc.servicio.implementacion.UsuarioServicioImp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProyectoGrpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoGrpcApplication.class, args);
	}


	// Bean que corre al iniciar la aplicación
	@Bean
	CommandLineRunner runGrpcServer(EntityManager em) {
		return args -> {

			// -------------------------
			// Instanciamos los DAOs con EntityManager (Spring lo inyecta)
			// -------------------------
			UsuarioDao usuarioDao = new UsuarioDaoImp(em);
			EventoSolidarioDao eventoDao = new EventoSolidarioDaoImp(em);
			InventarioDonacionesDao inventarioDao = new InventarioDonacionesDaoImp(em);

			// -------------------------
			// Instanciamos los servicios de negocio pasándoles los DAOs
			// -------------------------
			UsuarioServicio usuarioServicio = new UsuarioServicioImp(usuarioDao);
			EventoSolidarioServicio eventoServicio = new EventoSolidarioServicioImp(eventoDao);
			InventarioDonacionesServicio inventarioServicio = new InventarioDonacionesServicioImp(inventarioDao, usuarioDao);

			// -------------------------
			// Creamos los servicios gRPC pasándoles los servicios de negocio
			// -------------------------
			UsuarioServicioRpc usuarioGrpcService = new UsuarioServicioRpc(usuarioServicio);
			EventoSolidarioServicioRpc eventoGrpcService = new EventoSolidarioServicioRpc(eventoServicio, usuarioServicio);
			InventarioDonacionesServicioRpc inventarioGrpcService = new InventarioDonacionesServicioRpc(inventarioServicio);

			// -------------------------
			// Configuramos y levantamos el servidor gRPC
			// -------------------------
			Server server = ServerBuilder.forPort(9090)
					.addService(usuarioGrpcService)
					.addService(eventoGrpcService)
					.addService(inventarioGrpcService)
					.build()
					.start();

			System.out.println("Servidor gRPC iniciado en el puerto 9090...");

			// Mantener el servidor corriendo
			server.awaitTermination();
		};
	}
}


