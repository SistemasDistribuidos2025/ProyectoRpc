package org.example.ProyectoGrpc.servicio.implementacion;

import org.example.ProyectoGrpc.entidad.EventoSolidario;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.EventoSolidarioDao;
import org.example.ProyectoGrpc.servicio.EventoSolidarioServicio;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoSolidarioServicioImp implements EventoSolidarioServicio {

    private final EventoSolidarioDao eventoDao;

    public EventoSolidarioServicioImp(EventoSolidarioDao eventoDao) {
        this.eventoDao = eventoDao;
    }

    @Override
    public EventoSolidario altaEvento(EventoSolidario evento) {
        if (evento.getFechaHoraEvento().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden crear eventos pasados");
        }
        eventoDao.guardar(evento);
        return evento;
    }

    @Override
    public EventoSolidario modificarEvento(Long id, String nombre, String descripcion,
                                           LocalDateTime fechaHora, List<Usuario> participantes) {
        EventoSolidario evento = eventoDao.buscarPorId(id);
        if (evento == null) return null;

        evento.setNombreEvento(nombre);
        evento.setDescripcion(descripcion);
        evento.setFechaHoraEvento(fechaHora);
        evento.setParticipantesEvento(participantes);

        eventoDao.actualizar(evento);
        return evento;
    }

    @Override
    public EventoSolidario modificarEvento(Long id, EventoSolidario evento) {
        return modificarEvento(
                id,
                evento.getNombreEvento(),
                evento.getDescripcion(),
                evento.getFechaHoraEvento(),
                evento.getParticipantesEvento()
        );
    }

    @Override
    public void bajaEvento(Long id) {
        EventoSolidario evento = eventoDao.buscarPorId(id);
        if (evento != null && evento.getFechaHoraEvento().isAfter(LocalDateTime.now())) {
            eventoDao.eliminar(id);
        }
    }

    @Override
    public EventoSolidario buscarPorId(Long id) {
        return eventoDao.buscarPorId(id);
    }

    @Override
    public List<EventoSolidario> listarTodos() {
        return eventoDao.listarTodos();
    }

    @Override
    public void agregarMiembro(Long eventoId, Usuario usuario, String rolSolicitante) {
        EventoSolidario evento = eventoDao.buscarPorId(eventoId);
        if (evento == null) return;

        boolean puedeAgregar = rolSolicitante.equals("PRESIDENTE") ||
                rolSolicitante.equals("COORDINADOR") ||
                (rolSolicitante.equals("VOLUNTARIO") && evento.getParticipantesEvento().contains(usuario) == false);

        if (puedeAgregar && usuario.isActivo()) {
            evento.getParticipantesEvento().add(usuario);
            eventoDao.actualizar(evento);
        }
    }

    @Override
    public void quitarMiembro(Long eventoId, Usuario usuario, String rolSolicitante) {
        EventoSolidario evento = eventoDao.buscarPorId(eventoId);
        if (evento == null) return;

        boolean puedeQuitar = rolSolicitante.equals("PRESIDENTE") ||
                rolSolicitante.equals("COORDINADOR") ||
                (rolSolicitante.equals("VOLUNTARIO") && evento.getParticipantesEvento().contains(usuario));

        if (puedeQuitar) {
            evento.getParticipantesEvento().remove(usuario);
            eventoDao.actualizar(evento);
        }
    }
}
