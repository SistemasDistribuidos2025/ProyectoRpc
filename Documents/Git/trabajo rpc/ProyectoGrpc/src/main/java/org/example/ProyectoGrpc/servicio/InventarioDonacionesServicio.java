package org.example.ProyectoGrpc.servicio;

import org.example.ProyectoGrpc.entidad.InventarioDonaciones;
import org.example.ProyectoGrpc.enums.CategoriaDonacion;

import java.util.List;

public interface InventarioDonacionesServicio {
    InventarioDonaciones altaInventario(InventarioDonaciones inventario, Long usuarioAltaId);

    InventarioDonaciones modificarInventario(Long id, String nuevaDescripcion, int nuevaCantidad, Long usuarioModificacionId);

    void bajaInventario(Long id, Long usuarioModificacionId);

    InventarioDonaciones buscarPorId(Long id);

    List<InventarioDonaciones> listarTodos();

   
}
