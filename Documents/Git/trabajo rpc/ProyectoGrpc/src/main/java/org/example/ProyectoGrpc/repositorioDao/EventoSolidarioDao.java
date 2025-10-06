package org.example.ProyectoGrpc.repositorioDao;

import org.example.ProyectoGrpc.entidad.EventoSolidario;

import java.util.List;

public interface EventoSolidarioDao {
    void guardar(EventoSolidario evento);

    EventoSolidario buscarPorId(Long id);

    List<EventoSolidario> listarTodos();

    void actualizar(EventoSolidario evento);

    void eliminar(Long id);
}
