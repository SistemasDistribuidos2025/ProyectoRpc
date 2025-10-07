package org.example.ProyectoGrpc.repositorioDao;

import org.example.ProyectoGrpc.entidad.DonacionesEvento;

import java.util.List;

public interface DonacionesEventoDao {
    void guardar(DonacionesEvento donacion);
    DonacionesEvento buscarPorId(Long id);
    List<DonacionesEvento> listarPorEvento(Long eventoId);
    void actualizar(DonacionesEvento donacion);
    void eliminar(DonacionesEvento donacion);
}

