package com.madurez.back_software.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dominios")
public class Dominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "peso_relativo")
    private Double pesoRelativo = 0.0;

    @ManyToOne
    @JoinColumn(name = "instrumento_version_id", nullable = false)
    private InstrumentoVersion instrumentoVersion;

    public Dominio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPesoRelativo() { return pesoRelativo; }
    public void setPesoRelativo(Double pesoRelativo) { this.pesoRelativo = pesoRelativo; }

    public InstrumentoVersion getInstrumentoVersion() { return instrumentoVersion; }
    public void setInstrumentoVersion(InstrumentoVersion instrumentoVersion) { this.instrumentoVersion = instrumentoVersion; }
}