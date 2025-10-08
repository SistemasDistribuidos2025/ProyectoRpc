package org.example.ProyectoGrpc.servicio;

import org.example.ProyectoGrpc.entidad.EventoSolidario;
import org.example.ProyectoGrpc.entidad.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventoSolidarioServicio {
    EventoSolidario altaEvento(EventoSolidario evento);

    EventoSolidario modificarEvento(Long id, String nombre, String descripcion,
                                    LocalDateTime fechaHora, Set<Usuario> participantes);

    EventoSolidario modificarEvento(Long id, EventoSolidario evento);

    void bajaEvento(Long id);

    EventoSolidario buscarPorId(Long id);

    List<EventoSolidario> listarTodos();

    void agregarMiembro(Long eventoId, Usuario usuario, String rolSolicitante);

    void quitarMiembro(Long eventoId, Usuario usuario, String rolSolicitante);

    public void registrarDonacionEvento(Long eventoId, Long inventarioId, int cantidad, Long usuarioId);

}