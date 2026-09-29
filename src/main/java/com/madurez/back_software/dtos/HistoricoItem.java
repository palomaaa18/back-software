package com.madurez.back_software.dtos;

import java.time.LocalDateTime;

public class HistoricoItem {

    private Long evaluacionId;
    private LocalDateTime fechaFin;
    private Double coberturaGlobal;
    private String nivelMadurez;
    private String estado;

    public HistoricoItem(Long evaluacionId, LocalDateTime fechaFin, Double coberturaGlobal,
                         String nivelMadurez, String estado) {
        this.evaluacionId = evaluacionId;
        this.fechaFin = fechaFin;
        this.coberturaGlobal = coberturaGlobal;
        this.nivelMadurez = nivelMadurez;
        this.estado = estado;
    }

    public Long getEvaluacionId() { return evaluacionId; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public Double getCoberturaGlobal() { return coberturaGlobal; }
    public String getNivelMadurez() { return nivelMadurez; }
    public String getEstado() { return estado; }
}