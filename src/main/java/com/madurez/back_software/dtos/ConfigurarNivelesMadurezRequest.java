package com.madurez.back_software.dtos;

import java.util.List;

public class ConfigurarNivelesMadurezRequest {

    private List<NivelItem> niveles;

    public List<NivelItem> getNiveles() { return niveles; }
    public void setNiveles(List<NivelItem> niveles) { this.niveles = niveles; }

    public static class NivelItem {
        private String nombre;
        private Double rangoMin;
        private Double rangoMax;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public Double getRangoMin() { return rangoMin; }
        public void setRangoMin(Double rangoMin) { this.rangoMin = rangoMin; }

        public Double getRangoMax() { return rangoMax; }
        public void setRangoMax(Double rangoMax) { this.rangoMax = rangoMax; }
    }
}