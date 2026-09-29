package com.madurez.back_software.dtos;

public class GuardarRespuestaRequest {

    private Long preguntaId;
    private String nivelImplementacion; // NO_EXISTE / EXISTE_PARCIALMENTE / EXISTE_NO_FORMALIZADO / IMPLEMENTADO
    private String comentario;

    public Long getPreguntaId() { return preguntaId; }
    public void setPreguntaId(Long preguntaId) { this.preguntaId = preguntaId; }

    public String getNivelImplementacion() { return nivelImplementacion; }
    public void setNivelImplementacion(String nivelImplementacion) { this.nivelImplementacion = nivelImplementacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}