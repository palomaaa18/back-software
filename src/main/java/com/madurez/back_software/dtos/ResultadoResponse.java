package com.madurez.back_software.dtos;

import java.util.List;

public class ResultadoResponse {

    private Double coberturaGlobal;
    private String nivelMadurez;
    private List<ResultadoDominioItem> porDominio;

    public ResultadoResponse(Double coberturaGlobal, String nivelMadurez, List<ResultadoDominioItem> porDominio) {
        this.coberturaGlobal = coberturaGlobal;
        this.nivelMadurez = nivelMadurez;
        this.porDominio = porDominio;
    }

    public Double getCoberturaGlobal() { return coberturaGlobal; }
    public String getNivelMadurez() { return nivelMadurez; }
    public List<ResultadoDominioItem> getPorDominio() { return porDominio; }

    public static class ResultadoDominioItem {
        private Long dominioId;
        private String dominioNombre;
        private Double coberturaPorcentaje;
        private Double pesoRelativo;

        public ResultadoDominioItem(Long dominioId, String dominioNombre, Double coberturaPorcentaje, Double pesoRelativo) {
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