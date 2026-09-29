package com.madurez.back_software.dtos;

public class RespuestaResponse {

    private Long id;
    private Long preguntaId;
    private String preguntaTexto;
    private String nivelImplementacion;
    private String comentario;

    public RespuestaResponse(Long id, Long preguntaId, String preguntaTexto, String nivelImplementacion, String comentario) {
        this.id = id;
        this.preguntaId = preguntaId;
        this.preguntaTexto = preguntaTexto;
        this.nivelImplementacion = nivelImplementacion;
        this.comentario = comentario;
    }

    public Long getId() { return id; }
    public Long getPreguntaId() { return preguntaId; }
    public String getPreguntaTexto() { return preguntaTexto; }
    public String getNivelImplementacion() { return nivelImplementacion; }
    public String getComentario() { return comentario; }
}