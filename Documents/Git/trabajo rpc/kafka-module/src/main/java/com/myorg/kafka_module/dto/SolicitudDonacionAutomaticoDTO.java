package com.myorg.kafka_module.dto;

import java.util.List;

public class SolicitudDonacionAutomaticoDTO {
    private String idOrganizacion;
    private String idSolicitud;
    private List<ItemDonacionDTO> donaciones;

    public String getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(String idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public List<ItemDonacionDTO> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(List<ItemDonacionDTO> donaciones) {
        this.donaciones = donaciones;
    }

    public SolicitudDonacionAutomaticoDTO(String idOrganizacion, String idSolicitud, List<ItemDonacionDTO> donaciones) {
        this.idOrganizacion = idOrganizacion;
        this.idSolicitud = idSolicitud;
        this.donaciones = donaciones;
    }

    public SolicitudDonacionAutomaticoDTO(){}
}
