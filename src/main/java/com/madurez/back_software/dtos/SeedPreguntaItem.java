package com.madurez.back_software.dtos;

import java.util.List;
import java.util.Map;

public class SeedPreguntaItem {

    private String dominio;
    private String pregunta;
    private List<String> controlesISO27001;
    private List<String> controlesISO42001;
    private Map<String, String> recomendaciones; // clave: "INEXISTENTE" o "PARCIAL"

    public String getDominio() { return dominio; }
    public void setDominio(String dominio) { this.dominio = dominio; }

    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    public List<String> getControlesISO27001() { return controlesISO27001; }
    public void setControlesISO27001(List<String> controlesISO27001) { this.controlesISO27001 = controlesISO27001; }

    public List<String> getControlesISO42001() { return controlesISO42001; }
    public void setControlesISO42001(List<String> controlesISO42001) { this.controlesISO42001 = controlesISO42001; }

    public Map<String, String> getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(Map<String, String> recomendaciones) { this.recomendaciones = recomendaciones; }
}