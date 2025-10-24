package com.myorg.kafka_module.dto;

public class ItemSolicitudDTO {
    private String categoria;
    private String descripcion;


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ItemSolicitudDTO(String categoria, String descripcion) {
        this.categoria = categoria;
        this.descripcion = descripcion;
    }

    public ItemSolicitudDTO(){}
}
