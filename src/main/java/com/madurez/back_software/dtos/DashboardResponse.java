package com.madurez.back_software.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class DashboardResponse {

    private Long organizacionId;
    private String organizacionNombre;
    private LocalDateTime fechaEvaluacion;
    private Double coberturaGlobal;
    private String nivelMadurezNombre;
    private String tendencia; // SUBIO / BAJO / MANTUVO / SIN_COMPARATIVA
    private List<DominioItem> porDominio;

    public DashboardResponse(Long organizacionId, String organizacionNombre, LocalDateTime fechaEvaluacion,
                             Double coberturaGlobal, String nivelMadurezNombre, String tendencia,
                             List<DominioItem> porDominio) {
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.fechaEvaluacion = fechaEvaluacion;
        this.coberturaGlobal = coberturaGlobal;
        this.nivelMadurezNombre = nivelMadurezNombre;
        this.tendencia = tendencia;
        this.porDominio = porDominio;
    }

    public Long getOrganizacionId() { return organizacionId; }
    public String getOrganizacionNombre() { return organizacionNombre; }
    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public Double getCoberturaGlobal() { return coberturaGlobal; }
    public String getNivelMadurezNombre() { return nivelMadurezNombre; }
    public String getTendencia() { return tendencia; }
    public List<DominioItem> getPorDominio() { return porDominio; }

    public static class DominioItem {
        private Long dominioId;
        private String dominioNombre;
        private Double coberturaPorcentaje;
        private Double pesoRelativo;

        public DominioItem(Long dominioId, String dominioNombre, Double coberturaPorcentaje, Double pesoRelativo) {
            this.dominioId = dominioId;
            this.dominioNombre = dominioNombre;
            this.coberturaPorcentaje = coberturaPorcentaje;
            this.pesoRelativo = pesoRelativo;
        }

        public Long getDominioId() { return dominioId; }
        public String getDominioNombre() { return dominioNombre; }
        public Double getCoberturaPorcentaje() { return coberturaPorcentaje; }
        public Double getPesoRelativo() { return pesoRelativo; }
    }
}