package com.madurez.back_software.dtos;

public class NivelMadurezResponse {

    private Long id;
    private String nombre;
    private Double rangoMin;
    private Double rangoMax;

    public NivelMadurezResponse(Long id, String nombre, Double rangoMin, Double rangoMax) {
        this.id = id;
        this.nombre = nombre;
        this.rangoMin = rangoMin;
        this.rangoMax = rangoMax;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getRangoMin() { return rangoMin; }
    public Double getRangoMax() { return rangoMax; }
}