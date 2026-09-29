package com.madurez.back_software.dtos;

public class ProgresoDominioResponse {

    private Long dominioId;
    private String dominioNombre;
    private int totalPreguntas;
    private int respondidas;
    private double porcentajeAvance;

    public ProgresoDominioResponse(Long dominioId, String dominioNombre, int totalPreguntas, int respondidas) {
        this.dominioId = dominioId;
        this.dominioNombre = dominioNombre;
        this.totalPreguntas = totalPreguntas;
        this.respondidas = respondidas;
        this.porcentajeAvance = totalPreguntas == 0 ? 0.0 : (respondidas * 100.0) / totalPreguntas;
    }

    public Long getDominioId() { return dominioId; }
    public String getDominioNombre() { return dominioNombre; }
    public int getTotalPreguntas() { return totalPreguntas; }
    public int getRespondidas() { return respondidas; }
    public double getPorcentajeAvance() { return porcentajeAvance; }
}