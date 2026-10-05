package com.madurez.back_software.dtos;

public class RecomendacionPriorizadaResponse {

    private String dominioNombre;
    private Double pesoDominio;
    private String nivelRiesgo;
    private String controlAnexoA;
    private String preguntaTexto;
    private String recomendacionDescripcion;

    public RecomendacionPriorizadaResponse(String dominioNombre, Double pesoDominio, String nivelRiesgo,
                                           String controlAnexoA, String preguntaTexto, String recomendacionDescripcion) {
        this.dominioNombre = dominioNombre;
        this.pesoDominio = pesoDominio;
        this.nivelRiesgo = nivelRiesgo;
        this.controlAnexoA = controlAnexoA;
        this.preguntaTexto = preguntaTexto;
        this.recomendacionDescripcion = recomendacionDescripcion;
    }

    public String getDominioNombre() { return dominioNombre; }
    public Double getPesoDominio() { return pesoDominio; }
    public String getNivelRiesgo() { return nivelRiesgo; }
    public String getControlAnexoA() { return controlAnexoA; }
    public String getPreguntaTexto() { return preguntaTexto; }
    public String getRecomendacionDescripcion() { return recomendacionDescripcion; }
}