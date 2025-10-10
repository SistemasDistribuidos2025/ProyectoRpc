package com.myorg.kafka_module.dto;

import lombok.Data;

@Data
public class VoluntarioDTO {
    private String idOrganizacion;
    private String idVoluntario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
}
