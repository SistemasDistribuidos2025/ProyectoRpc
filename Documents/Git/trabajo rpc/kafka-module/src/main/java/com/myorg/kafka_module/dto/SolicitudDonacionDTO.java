package com.myorg.kafka_module.dto;

import java.util.List;

public class SolicitudDonacionDTO {


    private String idOrganizacion;


    private String idSolicitud;


    private List<ItemSolicitudDTO> donaciones;


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

    public List<ItemSolicitudDTO> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(List<ItemSolicitudDTO> donaciones) {
        this.donaciones = donaciones;
    }

    public SolicitudDonacionDTO(String idOrganizacion, String idSolicitud, List<ItemSolicitudDTO> donaciones) {
        this.idOrganizacion = idOrganizacion;
        this.idSolicitud = idSolicitud;
        this.donaciones = donaciones;
    }

    public SolicitudDonacionDTO(){}
}
