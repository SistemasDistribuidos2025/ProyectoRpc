package com.myorg.kafka_module.model;

import java.util.List;

public class SolicitudDonacion {


    private Long idOrganizacion;


    private Long idSolicitud;


    private List<ItemDonacionK> donaciones;


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

    public List<ItemDonacionK> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(List<ItemDonacionK> donaciones) {
        this.donaciones = donaciones;
    }

    public SolicitudDonacion(Long idOrganizacion, Long idSolicitud, List<ItemDonacionK> donaciones) {
        this.idOrganizacion = idOrganizacion;
        this.idSolicitud = idSolicitud;
        this.donaciones = donaciones;
    }

    public SolicitudDonacion(){}

}
