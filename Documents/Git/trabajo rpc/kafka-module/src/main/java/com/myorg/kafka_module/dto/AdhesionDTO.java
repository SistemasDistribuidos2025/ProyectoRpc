package com.myorg.kafka_module.dto;

import lombok.Data;

@Data
public class AdhesionDTO {
    private String idEvento;
    private String idOrganizador;
    private VoluntarioDTO voluntario;
}
