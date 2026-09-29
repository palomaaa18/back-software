package com.madurez.back_software.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class ComparativaResponse {

    private LocalDateTime fechaActual;
    private LocalDateTime fechaAnterior;
    private Double coberturaGlobalActual;
    private Double coberturaGlobalAnterior;
    private String nivelMadurezActual;
    private String nivelMadurezAnterior;
    private String tendenciaGlobal; // MEJORO / EMPEORO / IGUAL
    private List<DeltaDominio> deltasPorDominio;

    public ComparativaResponse(LocalDateTime fechaActual, LocalDateTime fechaAnterior,
                               Double coberturaGlobalActual, Double coberturaGlobalAnterior,
                               String nivelMadurezActual, String nivelMadurezAnterior,
                               String tendenciaGlobal, List<DeltaDominio> deltasPorDominio) {
        this.fechaActual = fechaActual;
        this.fechaAnterior = fechaAnterior;
        this.coberturaGlobalActual = coberturaGlobalActual;
        this.coberturaGlobalAnterior = coberturaGlobalAnterior;
        this.nivelMadurezActual = nivelMadurezActual;
        this.nivelMadurezAnterior = nivelMadurezAnterior;
        this.tendenciaGlobal = tendenciaGlobal;
        this.deltasPorDominio = deltasPorDominio;
    }

    public LocalDateTime getFechaActual() { return fechaActual; }
    public LocalDateTime getFechaAnterior() { return fechaAnterior; }
    public Double getCoberturaGlobalActual() { return coberturaGlobalActual; }
    public Double getCoberturaGlobalAnterior() { return coberturaGlobalAnterior; }
    public String getNivelMadurezActual() { return nivelMadurezActual; }
    public String getNivelMadurezAnterior() { return nivelMadurezAnterior; }
    public String getTendenciaGlobal() { return tendenciaGlobal; }
    public List<DeltaDominio> getDeltasPorDominio() { return deltasPorDominio; }

    public static class DeltaDominio {
        private String dominioNombre;
        private Double coberturaActual;
        private Double coberturaAnterior;
        private Double delta; // positivo = mejora, negativo = retroceso, 0 = sin cambio

        public DeltaDominio(String dominioNombre, Double coberturaActual, Double coberturaAnterior, Double delta) {
            this.dominioNombre = dominioNombre;
            this.coberturaActual = coberturaActual;
            this.coberturaAnterior = coberturaAnterior;
            this.delta = delta;
        }

        public String getDominioNombre() { return dominioNombre; }
        public Double getCoberturaActual() { return coberturaActual; }
        public Double getCoberturaAnterior() { return coberturaAnterior; }
        public Double getDelta() { return delta; }
    }
}