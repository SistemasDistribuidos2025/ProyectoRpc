package org.example.ProyectoGrpc.entidad;

import jakarta.persistence.*;
import org.example.ProyectoGrpc.enums.CategoriaDonacion;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario_donaciones")
public class InventarioDonaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaDonacion categoria;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private boolean eliminado = false;

    private LocalDateTime fechaHoraAlta;
    private LocalDateTime fechaHoraModificacion;

    @ManyToOne
    @JoinColumn(name = "usuario_alta_id")
    private Usuario usuarioAlta;

    @ManyToOne
    @JoinColumn(name = "usuario_modificado_id")
    private Usuario usuarioModificado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoriaDonacion getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDonacion categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(LocalDateTime fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public LocalDateTime getFechaHoraModificacion() {
        return fechaHoraModificacion;
    }

    public void setFechaHoraModificacion(LocalDateTime fechaHoraModificacion) {
        this.fechaHoraModificacion = fechaHoraModificacion;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuario getUsuarioModificado() {
        return usuarioModificado;
    }

    public void setUsuarioModificado(Usuario usuarioModificado) {
        this.usuarioModificado = usuarioModificado;
    }

    public InventarioDonaciones(Long id, CategoriaDonacion categoria, String descripcion, int cantidad, boolean eliminado,
                                LocalDateTime fechaHoraAlta, LocalDateTime fechaHoraModificacion, Usuario usuarioAlta, Usuario usuarioModificado) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.eliminado = eliminado;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraModificacion = fechaHoraModificacion;
        this.usuarioAlta = usuarioAlta;
        this.usuarioModificado = usuarioModificado;
    }

    public InventarioDonaciones(){}
}
