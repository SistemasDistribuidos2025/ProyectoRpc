package com.myorg.kafka_module.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TransferenciaDonacionDTO {
    private String idSolicitud;           // ID de la solicitud de donación

    private String idOrganizacionDonante; // Quién dona

    private String idOrganizacionReceptora; // A quién se transfiere

    private List<ItemDonacionDTO> donaciones; // Lista de items


    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getIdOrganizacionDonante() {
        return idOrganizacionDonante;
    }

    public void setIdOrganizacionDonante(String idOrganizacionDonante) {
        this.idOrganizacionDonante = idOrganizacionDonante;
    }

    public String getIdOrganizacionReceptora() {
        return idOrganizacionReceptora;
    }

    public void setIdOrganizacionReceptora(String idOrganizacionReceptora) {
        this.idOrganizacionReceptora = idOrganizacionReceptora;
    }

    public List<ItemDonacionDTO> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(List<ItemDonacionDTO> donaciones) {
        this.donaciones = donaciones;
    }

    public TransferenciaDonacionDTO(String idSolicitud, String idOrganizacionDonante, String idOrganizacionReceptora, List<ItemDonacionDTO> donaciones) {
        this.idSolicitud = idSolicitud;
        this.idOrganizacionDonante = idOrganizacionDonante;
        this.idOrganizacionReceptora = idOrganizacionReceptora;
        this.donaciones = donaciones;
    }

    public TransferenciaDonacionDTO(){}


}
