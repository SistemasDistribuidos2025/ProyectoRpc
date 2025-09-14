package org.example.ProyectoGrpc.entidad;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "eventos_solidarios")
public class EventoSolidario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreEvento;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaHoraEvento;

    @ManyToMany
    @JoinTable(
            name = "miembro_evento",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> participantesEvento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHoraEvento() {
        return fechaHoraEvento;
    }

    public void setFechaHoraEvento(LocalDateTime fechaHoraEvento) {
        this.fechaHoraEvento = fechaHoraEvento;
    }

    public List<Usuario> getParticipantesEvento() {
        return participantesEvento;
    }

    public void setParticipantesEvento(List<Usuario> participantesEvento) {
        this.participantesEvento = participantesEvento;
    }

    public EventoSolidario(Long id, String nombreEvento, String descripcion, LocalDateTime fechaHoraEvento, List<Usuario> participantesEvento) {
        this.id = id;
        this.nombreEvento = nombreEvento;
        this.descripcion = descripcion;
        this.fechaHoraEvento = fechaHoraEvento;
        this.participantesEvento = participantesEvento;
    }

    public EventoSolidario(){}
}
