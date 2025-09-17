package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import org.example.ProyectoGrpc.entidad.EventoSolidario;
import org.example.ProyectoGrpc.repositorioDao.EventoSolidarioDao;

import java.util.List;

public class EventoSolidarioDaoImp implements EventoSolidarioDao {

    private EntityManager em;

    public EventoSolidarioDaoImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public void guardar(EventoSolidario evento) {
        em.getTransaction().begin();
        em.persist(evento);
        em.getTransaction().commit();
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
        em.getTransaction().begin();
        em.merge(evento);
        em.getTransaction().commit();
    }

    @Override
    public void eliminar(Long id) {
        em.getTransaction().begin();
        EventoSolidario evento = em.find(EventoSolidario.class, id);
        if (evento != null) {
            em.remove(evento);
        }
        em.getTransaction().commit();
    }
}

