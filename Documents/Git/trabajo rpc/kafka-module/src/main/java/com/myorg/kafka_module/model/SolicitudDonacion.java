package com.myorg.kafka_module.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SolicitudDonacion {


    private Long idOrganizacion;


    private Long idSolicitud;


    private List<ItemDonacion> donaciones;


    public Long getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(Long idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public List<ItemDonacion> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(List<ItemDonacion> donaciones) {
        this.donaciones = donaciones;
    }

    public SolicitudDonacion(Long idOrganizacion, Long idSolicitud, List<ItemDonacion> donaciones) {
        this.idOrganizacion = idOrganizacion;
        this.idSolicitud = idSolicitud;
        this.donaciones = donaciones;
    }

    public SolicitudDonacion(){}

}
