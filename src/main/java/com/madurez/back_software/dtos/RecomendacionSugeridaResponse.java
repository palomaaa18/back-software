package com.madurez.back_software.dtos;

public class RecomendacionSugeridaResponse {

    private Long preguntaId;
    private String preguntaTexto;
    private String dominioNombre;
    private String nivelRespondido;
    private String recomendacionDescripcion;

    public RecomendacionSugeridaResponse(Long preguntaId, String preguntaTexto, String dominioNombre,
                                         String nivelRespondido, String recomendacionDescripcion) {
        this.preguntaId = preguntaId;
        this.preguntaTexto = preguntaTexto;
        this.dominioNombre = dominioNombre;
        this.nivelRespondido = nivelRespondido;
        this.recomendacionDescripcion = recomendacionDescripcion;
    }

    public Long getPreguntaId() { return preguntaId; }
    public String getPreguntaTexto() { return preguntaTexto; }
    public String getDominioNombre() { return dominioNombre; }
    public String getNivelRespondido() { return nivelRespondido; }
    public String getRecomendacionDescripcion() { return recomendacionDescripcion; }
}