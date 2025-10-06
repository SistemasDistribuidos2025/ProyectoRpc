package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.ProyectoGrpc.entidad.EventoSolidario;
import org.example.ProyectoGrpc.repositorioDao.EventoSolidarioDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventoSolidarioDaoImp implements EventoSolidarioDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void guardar(EventoSolidario evento) {
        em.persist(evento);
    }

    @Override
    public EventoSolidario buscarPorId(Long id) {
        return em.find(EventoSolidario.class, id);
    }

    //Query que trae los datos del evento junto con los de la tabla intermedia de participantes
    @Override
    public List<EventoSolidario> listarTodos() {
        return em.createQuery("SELECT DISTINCT e FROM EventoSolidario e LEFT JOIN FETCH e.participantesEvento", EventoSolidario.class)
                .getResultList();
    }

    @Override
    public void actualizar(EventoSolidario evento) {
        em.merge(evento);
    }

    @Override
    public void eliminar(Long id) {
        EventoSolidario evento = em.find(EventoSolidario.class, id);
        if (evento != null) {
            em.remove(evento);
        }
    }
}