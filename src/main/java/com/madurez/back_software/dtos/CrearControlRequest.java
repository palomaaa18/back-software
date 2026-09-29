package com.madurez.back_software.dtos;

public class CrearControlRequest {

    private String norma;    // "ISO_27001" o "ISO_42001"
    private String anexoA;
    private Long dominioId;

    public CrearControlRequest() {}

    public String getNorma() { return norma; }
    public void setNorma(String norma) { this.norma = norma; }

    public String getAnexoA() { return anexoA; }
    public void setAnexoA(String anexoA) { this.anexoA = anexoA; }

    public Long getDominioId() { return dominioId; }
    public void setDominioId(Long dominioId) { this.dominioId = dominioId; }
}