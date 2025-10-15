package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.ProyectoGrpc.entidad.DonacionesEvento;
import org.example.ProyectoGrpc.repositorioDao.DonacionesEventoDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DonacionesEventoDaoImp implements DonacionesEventoDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void guardar(DonacionesEvento donacion) {
        em.persist(donacion);
    }

    @Override
    public DonacionesEvento buscarPorId(Long id) {
        return em.find(DonacionesEvento.class, id);
    }

    @Override
    public List<DonacionesEvento> listarPorEvento(Long eventoId) {
        return em.createQuery(
                        "SELECT d FROM DonacionesEvento d WHERE d.evento.id = :eventoId", DonacionesEvento.class)
                .setParameter("eventoId", eventoId)
                .getResultList();
    }

    @Override
    public void actualizar(DonacionesEvento donacion) {
        em.merge(donacion);
    }

    @Override
    public void eliminar(DonacionesEvento donacion) {
        em.remove(em.contains(donacion) ? donacion : em.merge(donacion));
    }

    
}
