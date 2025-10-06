package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.UsuarioDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.NoResultException;


import java.util.List;

@Repository
public class UsuarioDaoImp implements UsuarioDao {

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Usuario buscarPorIdentificador(String identificador) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :identificador OR u.nombreUsuario = :identificador",
            Usuario.class
        );
        query.setParameter("identificador", identificador);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    
    }

    @Override
    @Transactional
    public void guardar(Usuario usuario) {
        em.persist(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }
    
	
    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario",
            Usuario.class
        );
        query.setParameter("nombreUsuario", nombreUsuario);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email",
            Usuario.class
        );
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);
    }

		 
    @Override
    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Override
    public void actualizar(Usuario usuario) {
        em.merge(usuario);
    }

    @Override
    @Transactional
    public void darDeBaja(Long id) {
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            usuario.setActivo(false);
            em.merge(usuario);
        }
    }
    
    

	@Override
	public Usuario buscarPorIdentificadorYPassword(String identificador, String password) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
    
    

    



