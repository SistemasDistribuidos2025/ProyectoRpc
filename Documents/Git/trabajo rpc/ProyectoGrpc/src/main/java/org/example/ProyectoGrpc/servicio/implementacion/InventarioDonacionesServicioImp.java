package org.example.ProyectoGrpc.servicio.implementacion;

import org.example.ProyectoGrpc.entidad.InventarioDonaciones;
import org.example.ProyectoGrpc.entidad.Usuario;
import org.example.ProyectoGrpc.repositorioDao.InventarioDonacionesDao;
import org.example.ProyectoGrpc.repositorioDao.UsuarioDao;
import org.example.ProyectoGrpc.servicio.InventarioDonacionesServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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

    @Transactional
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

    @Transactional
    @Override
    public void bajaInventario(Long id, Long usuarioModificacionId) {
        // Cargar entidad completa desde la DB
        InventarioDonaciones inventario = inventarioDao.buscarPorId(id);
        if (inventario == null) {
            throw new IllegalArgumentException("Inventario no encontrado");
        }

        // Solo modificar los campos de la baja
        inventario.setEliminado(true);
        inventario.setFechaHoraModificacion(LocalDateTime.now());

        if (usuarioModificacionId != null && usuarioModificacionId != 0) {
            Usuario usuarioModificacion = usuarioDao.buscarPorId(usuarioModificacionId);
            inventario.setUsuarioModificado(usuarioModificacion);
        }

        // No llamar a merge manualmente, Hibernate detecta el cambio porque la entidad está gestionada
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
