package com.myorg.kafka_module.dto;

public class ItemDonacionDTO {


    private String categoria;
    private String descripcion;
    private int cantidad;


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

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

    public ItemDonacionDTO(String categoria, String descripcion, int cantidad) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public ItemDonacionDTO(){}
}
