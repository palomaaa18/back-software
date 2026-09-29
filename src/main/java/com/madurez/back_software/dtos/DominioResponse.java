package com.madurez.back_software.dtos;

public class DominioResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double pesoRelativo;

    public DominioResponse(Long id, String nombre, String descripcion, Double pesoRelativo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pesoRelativo = pesoRelativo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPesoRelativo() { return pesoRelativo; }
}