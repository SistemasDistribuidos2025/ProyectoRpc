package org.example.ProyectoGrpc.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "donaciones_evento")
public class DonacionesEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "inventario_id")
    private InventarioDonaciones item;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private EventoSolidario evento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public InventarioDonaciones getItem() {
        return item;
    }

    public void setItem(InventarioDonaciones item) {
        this.item = item;
    }

    public EventoSolidario getEvento() {
        return evento;
    }

    public void setEvento(EventoSolidario evento) {
        this.evento = evento;
    }

    public DonacionesEvento(Long id, int cantidad, Usuario usuario, InventarioDonaciones item, EventoSolidario evento) {
        this.id = id;
        this.cantidad = cantidad;
        this.usuario = usuario;
        this.item = item;
        this.evento = evento;
    }

    public DonacionesEvento(){}
}