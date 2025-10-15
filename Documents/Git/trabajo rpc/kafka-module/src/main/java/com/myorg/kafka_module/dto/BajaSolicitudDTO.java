package com.myorg.kafka_module.dto;

import lombok.Data;

@Data
public class BajaSolicitudDTO {
    private String idOrganizacionBaja;
    private String idSolicitudBaja;

    public String getIdOrganizacionBaja() {
        return idOrganizacionBaja;
    }
    public void setIdOrganizacionBaja(String idOrganizacionBaja) {
        this.idOrganizacionBaja = idOrganizacionBaja;
    }
    public String getIdSolicitudBaja() {
        return idSolicitudBaja;
    }
    public void setIdSolicitudBaja(String idSolicitudBaja) {
        this.idSolicitudBaja = idSolicitudBaja;
    }

    public BajaSolicitudDTO() {
    }

    public BajaSolicitudDTO(String idOrganizacionBaja, String idSolicitudBaja) {
        this.idOrganizacionBaja = idOrganizacionBaja;
        this.idSolicitudBaja = idSolicitudBaja;
    }
}
