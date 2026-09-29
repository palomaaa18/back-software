package com.madurez.back_software.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "niveles_madurez")
public class NivelMadurez {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre; // Inicial, Básico, Definido, Gestionado, Optimizado

    @Column(name = "rango_min", nullable = false)
    private Double rangoMin;

    @Column(name = "rango_max", nullable = false)
    private Double rangoMax;

    public NivelMadurez() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getRangoMin() { return rangoMin; }
    public void setRangoMin(Double rangoMin) { this.rangoMin = rangoMin; }

    public Double getRangoMax() { return rangoMax; }
    public void setRangoMax(Double rangoMax) { this.rangoMax = rangoMax; }
}