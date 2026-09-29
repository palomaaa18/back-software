package com.madurez.back_software.dtos;

import java.util.List;

public class CrearPreguntaRequest {

    private String texto;
    private Long dominioId;
    private List<Long> controlIds;

    public CrearPreguntaRequest() {}

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Long getDominioId() { return dominioId; }
    public void setDominioId(Long dominioId) { this.dominioId = dominioId; }

    public List<Long> getControlIds() { return controlIds; }
    public void setControlIds(List<Long> controlIds) { this.controlIds = controlIds; }
}