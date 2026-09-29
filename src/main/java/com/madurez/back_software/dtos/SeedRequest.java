package com.madurez.back_software.dtos;

import java.util.List;

public class SeedRequest {

    private List<SeedPreguntaItem> preguntas;

    public List<SeedPreguntaItem> getPreguntas() { return preguntas; }
    public void setPreguntas(List<SeedPreguntaItem> preguntas) { this.preguntas = preguntas; }
}