package org.example.ProyectoGrpc.config;

import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.enums.RolUsuario;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PrimerUsuarioLoader implements CommandLineRunner {

    private final UsuarioServicio usuarioServicio;

    public PrimerUsuarioLoader(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("Usuarios antes de crear presidente: " + usuarioServicio.listarTodos().size());

        boolean presidenteExiste = usuarioServicio.listarTodos()
                .stream()
                .anyMatch(u -> u.getRol() == RolUsuario.PRESIDENTE);

        if (!presidenteExiste) {
            Usuario presidente = new Usuario();
            presidente.setNombre("Juan");
            presidente.setApellido("Perez");
            presidente.setNombreUsuario("presidente");
            presidente.setEmail("magosh90@gmail.com");
            presidente.setRol(RolUsuario.PRESIDENTE);

            String passwordPlano = "Presidente123";
            String passwordEncriptada = PasswordUtils.encriptarPassword(passwordPlano);
            presidente.setPassword(passwordEncriptada);

            usuarioServicio.guardarPrimerUsuario(presidente, passwordPlano);

            System.out.println("Usuarios después: " + usuarioServicio.listarTodos().size() +
                    ", id: " + presidente.getId());
            System.out.println("Usuario PRESIDENTE creado con contraseña encriptada");
        }
    }
}