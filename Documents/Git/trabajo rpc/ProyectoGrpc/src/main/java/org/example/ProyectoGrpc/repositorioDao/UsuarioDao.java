package org.example.ProyectoGrpc.repositorioDao;

import org.example.ProyectoGrpc.entidad.Usuario;

import java.util.List;

public interface UsuarioDao {

    void guardar(Usuario usuario);

    Usuario buscarPorId(Long id);

    Usuario buscarPorEmail(String email);

    Usuario buscarPorNombreUsuario(String nombreUsuario);

    List<Usuario> listarTodos();

    void actualizar(Usuario usuario);

    void darDeBaja(Long id);
    
    Usuario buscarPorIdentificadorYPassword(String identificador, String password);
    
    Usuario buscarPorIdentificador(String identificador);
   
}
