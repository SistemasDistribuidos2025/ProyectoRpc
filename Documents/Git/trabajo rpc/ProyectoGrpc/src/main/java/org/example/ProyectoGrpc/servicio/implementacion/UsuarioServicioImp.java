package org.example.ProyectoGrpc.servicio.implementacion;

import org.example.ProyectoGrpc.config.PasswordUtils;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.UsuarioDao;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;

import jakarta.transaction.Transactional;

import java.util.List;

public class UsuarioServicioImp implements UsuarioServicio {
    private final UsuarioDao usuarioDao;

    public UsuarioServicioImp(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public Usuario altaUsuario(Usuario usuario) {

        if (usuarioDao.buscarPorEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        if (usuarioDao.buscarPorNombreUsuario(usuario.getNombreUsuario()) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Generar clave random y encriptar
        String randomPassword = PasswordUtils.generarPasswordAleatoria(8);
        String encryptedPassword = PasswordUtils.encriptarPassword(randomPassword);
        usuario.setPassword(encryptedPassword);
        usuario.setActivo(true);

        // TODO: enviar el password por email al usuario
        // EmailService.enviarPassword(usuario.getEmail(), randomPassword);

        usuarioDao.guardar(usuario);
        return usuario;
    }

    @Override
    public Usuario modificarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario existente = usuarioDao.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        existente.setNombre(usuarioActualizado.getNombre());
        existente.setApellido(usuarioActualizado.getApellido());
        existente.setTelefono(usuarioActualizado.getTelefono());
        existente.setEmail(usuarioActualizado.getEmail());
        existente.setRol(usuarioActualizado.getRol());
        existente.setActivo(usuarioActualizado.isActivo());

        usuarioDao.actualizar(existente);
        return existente;
    }

    @Override
    public void bajaUsuario(Long id) {
        usuarioDao.darDeBaja(id);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioDao.buscarPorId(id);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioDao.buscarPorEmail(email);
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioDao.buscarPorNombreUsuario(nombreUsuario);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioDao.listarTodos();
    }
 
    public Usuario login(String identificador, String password) {
        Usuario usuario = usuarioDao.buscarPorIdentificadorYPassword(identificador, password);

        if (usuario == null) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return usuario;
    }
    
}
