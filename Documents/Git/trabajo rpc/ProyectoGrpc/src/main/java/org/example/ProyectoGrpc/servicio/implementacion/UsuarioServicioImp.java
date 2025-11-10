package org.example.ProyectoGrpc.servicio.implementacion;

import org.example.ProyectoGrpc.config.PasswordUtils;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.UsuarioDao;
import org.example.ProyectoGrpc.servicio.UsuarioServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UsuarioServicioImp implements UsuarioServicio {
    private final UsuarioDao usuarioDao;
    @Autowired
    private EmailService emailService;

    public UsuarioServicioImp(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    @Transactional
    public Usuario altaUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        if (usuarioDao.buscarPorEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        if (usuarioDao.buscarPorNombreUsuario(usuario.getNombreUsuario()) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Generar contraseña aleatoria y encriptarla
        String randomPassword = PasswordUtils.generarPasswordAleatoria(8);
        String encryptedPassword = PasswordUtils.encriptarPassword(randomPassword);
        usuario.setPassword(encryptedPassword);

        usuario.setActivo(true);
        usuarioDao.guardar(usuario);

        emailService.enviarPassword(usuario.getEmail(), randomPassword);

        return usuario;
    }

    @Override
    @Transactional
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
    @Transactional
    public Usuario bajaUsuario(Long id) {

        Usuario existente = usuarioDao.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        existente.setActivo(false);

        usuarioDao.actualizar(existente);
        return existente;
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

    @Transactional
    @Override
    public Usuario login(String identificador, String passwordPlano) {
        Usuario usuario = usuarioDao.buscarPorIdentificador(identificador);

        if (usuario == null) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        // ✅ Verificación correcta con BCrypt
        if (!PasswordUtils.verificarPassword(passwordPlano, usuario.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return usuario;
    }

    @Override
    @Transactional
    public Usuario guardarPrimerUsuario(Usuario usuario, String passwordPlano) {
        usuario.setActivo(true);
        usuarioDao.guardar(usuario);

        System.out.println("Primer usuario creado: " + usuario.getNombreUsuario() +
                " / Password (plano): " + passwordPlano);

        return usuario;
    }

}
