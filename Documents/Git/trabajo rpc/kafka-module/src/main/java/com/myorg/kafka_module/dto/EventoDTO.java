package com.myorg.kafka_module.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EventoDTO {
    private String idOrganizacion;
    private String idEvento;
    private String nombreEvento;
    private String descripcion;
    private LocalDateTime fechaHora;

    
}
