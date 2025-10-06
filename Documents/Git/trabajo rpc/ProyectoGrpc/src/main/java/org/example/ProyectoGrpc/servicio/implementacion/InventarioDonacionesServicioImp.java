package org.example.ProyectoGrpc.servicio.implementacion;

import org.example.ProyectoGrpc.entidad.InventarioDonaciones;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.enums.CategoriaDonacion;
import org.example.ProyectoGrpc.repositorioDao.InventarioDonacionesDao;
import org.example.ProyectoGrpc.repositorioDao.UsuarioDao;
import org.example.ProyectoGrpc.servicio.InventarioDonacionesServicio;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class InventarioDonacionesServicioImp implements InventarioDonacionesServicio {

    private final InventarioDonacionesDao inventarioDao;
    private final UsuarioDao usuarioDao;

    public InventarioDonacionesServicioImp(InventarioDonacionesDao inventarioDao, UsuarioDao usuarioDao) {
        this.inventarioDao = inventarioDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    public InventarioDonaciones altaInventario(InventarioDonaciones inventario, Long usuarioAltaId) {
        Usuario usuarioAlta = usuarioDao.buscarPorId(usuarioAltaId);
        if (usuarioAlta == null) {
            throw new IllegalArgumentException("Usuario de alta no encontrado");
        }

        inventario.setUsuarioAlta(usuarioAlta);
        inventario.setFechaHoraAlta(LocalDateTime.now());
        inventario.setEliminado(false);

        inventarioDao.guardar(inventario);
        return inventario;
    }

    @Override
    public InventarioDonaciones modificarInventario(Long id, String nuevaDescripcion, int nuevaCantidad, Long usuarioModificacionId) {
        InventarioDonaciones inventario = inventarioDao.buscarPorId(id);
        if (inventario == null) {
            throw new IllegalArgumentException("Inventario no encontrado");
        }

        Usuario usuarioModificacion = usuarioDao.buscarPorId(usuarioModificacionId);
        if (usuarioModificacion == null) {
            throw new IllegalArgumentException("Usuario de modificación no encontrado");
        }

        if (nuevaCantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        inventario.setDescripcion(nuevaDescripcion);
        inventario.setCantidad(nuevaCantidad);
        inventario.setUsuarioModificado(usuarioModificacion);
        inventario.setFechaHoraModificacion(LocalDateTime.now());

        inventarioDao.actualizar(inventario);
        return inventario;
    }

    @Override
    public void bajaInventario(Long id, Long usuarioModificacionId) {
        inventarioDao.eliminarLogico(id, usuarioModificacionId);
    }

    @Override
    public InventarioDonaciones buscarPorId(Long id) {
        return inventarioDao.buscarPorId(id);
    }

    @Override
    public List<InventarioDonaciones> listarTodos() {
        return inventarioDao.listarTodos();
    }
}
