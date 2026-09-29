package com.madurez.back_software.dtos;

import java.util.List;

public class PreguntaResponse {

    private Long id;
    private String texto;
    private Long dominioId;
    private String dominioNombre;
    private List<ControlResponse> controles;

    public PreguntaResponse(Long id, String texto, Long dominioId, String dominioNombre, List<ControlResponse> controles) {
        this.id = id;
        this.texto = texto;
        this.dominioId = dominioId;
        this.dominioNombre = dominioNombre;
        this.controles = controles;
    }

    public Long getId() { return id; }
    public String getTexto() { return texto; }
    public Long getDominioId() { return dominioId; }
    public String getDominioNombre() { return dominioNombre; }
    public List<ControlResponse> getControles() { return controles; }
}