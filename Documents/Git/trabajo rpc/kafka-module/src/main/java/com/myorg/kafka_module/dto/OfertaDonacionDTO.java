package com.myorg.kafka_module.dto;

import java.util.List;

public class OfertaDonacionDTO {

    private String idOferta;                // ID de la oferta
    private String idOrganizacionDonante;   // Organización que dona
    private List<ItemDonacionDTO> donaciones; // Lista de items ofrecidos


    public String getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(String idOferta) {
        this.idOferta = idOferta;
    }

    public String getIdOrganizacionDonante() {
        return idOrganizacionDonante;
    }

    public void setIdOrganizacionDonante(String idOrganizacionDonante) {
        this.idOrganizacionDonante = idOrganizacionDonante;
    }

    public List<ItemDonacionDTO> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(List<ItemDonacionDTO> donaciones) {
        this.donaciones = donaciones;
    }


    public OfertaDonacionDTO(String idOferta, String idOrganizacionDonante, List<ItemDonacionDTO> donaciones) {
        this.idOferta = idOferta;
        this.idOrganizacionDonante = idOrganizacionDonante;
        this.donaciones = donaciones;
    }

    public OfertaDonacionDTO(){}
}
