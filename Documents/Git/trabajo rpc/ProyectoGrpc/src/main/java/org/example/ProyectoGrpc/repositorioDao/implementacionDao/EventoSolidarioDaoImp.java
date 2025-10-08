package org.example.ProyectoGrpc.repositorioDao.implementacionDao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
        try {
            return em.createQuery(
                            "SELECT e FROM EventoSolidario e LEFT JOIN FETCH e.participantesEvento WHERE e.id = :id",
                            EventoSolidario.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // si no encuentra el evento
        }
    }

    //Query que trae los datos del evento junto con los de la tabla intermedia de participantes
    @Override
    public List<EventoSolidario> listarTodos() {
        return em.createQuery("SELECT DISTINCT e FROM EventoSolidario e LEFT JOIN FETCH e.participantesEvento", EventoSolidario.class)
                .getResultList();
    }

    @Override
    public void actualizar(EventoSolidario evento) {
        EventoSolidario managed = em.find(EventoSolidario.class, evento.getId());
        if (managed != null) {
            managed.setNombreEvento(evento.getNombreEvento());
            managed.setDescripcion(evento.getDescripcion());
            managed.setFechaHoraEvento(evento.getFechaHoraEvento());
            managed.setParticipantesEvento(evento.getParticipantesEvento());
            em.flush();
        }
    }

    @Override
    public void eliminar(Long id) {
        EventoSolidario evento = em.find(EventoSolidario.class, id);
        if (evento != null) {
            // Limpia la relación ManyToMany
            evento.getParticipantesEvento().clear();
            em.remove(evento);
        }
    }
}