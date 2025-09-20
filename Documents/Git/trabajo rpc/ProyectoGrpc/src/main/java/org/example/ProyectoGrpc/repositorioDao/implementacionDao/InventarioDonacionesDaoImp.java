package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.ProyectoGrpc.entidad.InventarioDonaciones;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.InventarioDonacionesDao;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class InventarioDonacionesDaoImp implements InventarioDonacionesDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void guardar(InventarioDonaciones inventario) {
        inventario.setFechaHoraAlta(LocalDateTime.now());
        em.persist(inventario);
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
        inventario.setFechaHoraModificacion(LocalDateTime.now());
        em.merge(inventario);
    }

    @Override
    public void eliminarLogico(Long id, Long usuarioModificacionId) {
        InventarioDonaciones inventario = em.find(InventarioDonaciones.class, id);
        if (inventario != null) {
            inventario.setEliminado(true);
            inventario.setFechaHoraModificacion(LocalDateTime.now());
            Usuario usuario = em.find(Usuario.class, usuarioModificacionId);
            inventario.setUsuarioModificado(usuario);
            em.merge(inventario);
        }
    }
}

