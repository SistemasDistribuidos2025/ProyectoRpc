package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.UsuarioDao;

import java.util.List;

public class UsuarioDaoImp implements UsuarioDao {

    private EntityManager em;

    public UsuarioDaoImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public void guardar(Usuario usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario", Usuario.class);
        query.setParameter("nombreUsuario", nombreUsuario);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Override
    public void actualizar(Usuario usuario) {
        em.getTransaction().begin();
        em.merge(usuario);
        em.getTransaction().commit();
    }

    @Override
    public void darDeBaja(Long id) {
        em.getTransaction().begin();
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            usuario.setActivo(false);
            em.merge(usuario);
        }
        em.getTransaction().commit();
    }
    
    @Override
    public Usuario buscarPorIdentificadorYPassword(String identificador, String password) {
    	
        TypedQuery<Usuario> query = em.createQuery(
        		
            "SELECT u FROM Usuario u " +
            "WHERE (u.email = :identificador OR u.nombreUsuario = :identificador) " +			
            "AND u.password = :password",
            Usuario.class
        );
        
        query.setParameter("identificador", identificador);
        query.setParameter("password", password);

        return query.getSingleResult();
    }
    
}
