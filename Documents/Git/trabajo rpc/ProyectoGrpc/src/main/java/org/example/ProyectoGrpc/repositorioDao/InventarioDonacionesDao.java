package org.example.ProyectoGrpc.repositorioDao;

import org.example.ProyectoGrpc.entidad.InventarioDonaciones;
import org.example.ProyectoGrpc.enums.CategoriaDonacion;

import java.util.List;

public interface InventarioDonacionesDao {
    void guardar(InventarioDonaciones inventario);

    InventarioDonaciones buscarPorId(Long id);

    List<InventarioDonaciones> listarTodos();

    void actualizar(InventarioDonaciones inventario);

    void eliminarLogico(Long id, Long usuarioModificacionId);

    InventarioDonaciones buscarPorCategoriaYDescripcion(CategoriaDonacion categoria, String descripcion);
}


