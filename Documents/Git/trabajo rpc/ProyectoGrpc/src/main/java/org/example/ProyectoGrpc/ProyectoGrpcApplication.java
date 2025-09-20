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
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ProyectoGrpcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoGrpcApplication.class, args);
	}
}
