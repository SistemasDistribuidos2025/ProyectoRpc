package org.example.ProyectoGrpc.servicio;

import org.example.ProyectoGrpc.entidad.Usuario;

import java.util.List;

public interface UsuarioServicio {
    Usuario altaUsuario(Usuario usuario);

    Usuario modificarUsuario(Long id, Usuario usuarioActualizado);

    Usuario bajaUsuario(Long id);

    Usuario buscarPorId(Long id);

    Usuario buscarPorEmail(String email);

    Usuario buscarPorNombreUsuario(String nombreUsuario);

    List<Usuario> listarTodos();

    Usuario login(String identificador, String password);

    Usuario guardarPrimerUsuario(Usuario usuario, String passwordPlano);
}
