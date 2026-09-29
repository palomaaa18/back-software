package com.madurez.back_software.dtos;

public class RecomendacionResponse {

    private Long id;
    private String descripcion;
    private Long preguntaId;
    private String preguntaTexto;
    private String nivel;

    public RecomendacionResponse(Long id, String descripcion, Long preguntaId, String preguntaTexto, String nivel) {
        this.id = id;
        this.descripcion = descripcion;
        this.preguntaId = preguntaId;
        this.preguntaTexto = preguntaTexto;
        this.nivel = nivel;
    }

    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public Long getPreguntaId() { return preguntaId; }
    public String getPreguntaTexto() { return preguntaTexto; }
    public String getNivel() { return nivel; }
}