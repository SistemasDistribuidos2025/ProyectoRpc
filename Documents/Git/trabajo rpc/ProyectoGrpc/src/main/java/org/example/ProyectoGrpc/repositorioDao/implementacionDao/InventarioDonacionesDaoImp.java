package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.ProyectoGrpc.entidad.InventarioDonaciones;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.InventarioDonacionesDao;

import java.time.LocalDateTime;
import java.util.List;

public class InventarioDonacionesDaoImp implements InventarioDonacionesDao {
    private final EntityManager em;

    public InventarioDonacionesDaoImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public void guardar(InventarioDonaciones inventario) {
        em.getTransaction().begin();
        inventario.setFechaHoraAlta(LocalDateTime.now());
        em.persist(inventario);
        em.getTransaction().commit();
    }

    @Override
    public InventarioDonaciones buscarPorId(Long id) {
        return em.find(InventarioDonaciones.class, id);
    }

    @Override
    public List<InventarioDonaciones> listarTodos() {
        TypedQuery<InventarioDonaciones> query = em.createQuery(
                "SELECT i FROM InventarioDonaciones i WHERE i.eliminado = false", InventarioDonaciones.class);
        return query.getResultList();
    }

    @Override
    public void actualizar(InventarioDonaciones inventario) {
        em.getTransaction().begin();
        inventario.setFechaHoraModificacion(LocalDateTime.now());
        em.merge(inventario);
        em.getTransaction().commit();
    }

    @Override
    public void eliminarLogico(Long id, Long usuarioModificacionId) {
        em.getTransaction().begin();
        InventarioDonaciones inventario = em.find(InventarioDonaciones.class, id);
        if (inventario != null) {
            inventario.setEliminado(true);
            inventario.setFechaHoraModificacion(LocalDateTime.now());
            Usuario usuario = em.find(Usuario.class, usuarioModificacionId);
            inventario.setUsuarioModificado(usuario);
            em.merge(inventario);
        }
        em.getTransaction().commit();
    }
}

