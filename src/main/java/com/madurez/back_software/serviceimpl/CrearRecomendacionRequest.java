package com.madurez.back_software.serviceimpl;

public class CrearRecomendacionRequest {

    private Long preguntaId;
    private String nivel; // NO_EXISTE o EXISTE_PARCIALMENTE
    private String descripcion;

    public Long getPreguntaId() { return preguntaId; }
    public void setPreguntaId(Long preguntaId) { this.preguntaId = preguntaId; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}