package com.madurez.back_software.dtos;

public class ControlResponse {

    private Long id;
    private String norma;
    private String anexoA;
    private Long dominioId;
    private String dominioNombre;

    public ControlResponse(Long id, String norma, String anexoA, Long dominioId, String dominioNombre) {
        this.id = id;
        this.norma = norma;
        this.anexoA = anexoA;
        this.dominioId = dominioId;
        this.dominioNombre = dominioNombre;
    }

    public Long getId() { return id; }
    public String getNorma() { return norma; }
    public String getAnexoA() { return anexoA; }
    public Long getDominioId() { return dominioId; }
    public String getDominioNombre() { return dominioNombre; }
}