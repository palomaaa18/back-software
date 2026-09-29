package com.madurez.back_software.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "resultados")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    // Nulo cuando es_global = true
    @ManyToOne
    @JoinColumn(name = "dominio_id")
    private Dominio dominio;

    @Column(name = "cobertura_porcentaje", nullable = false)
    private Double coberturaPorcentaje;

    @Column(name = "es_global", nullable = false)
    private boolean esGlobal = false;

    // Solo se asigna cuando es_global = true (HU0024)
    @ManyToOne
    @JoinColumn(name = "nivel_madurez_id")
    private NivelMadurez nivelMadurez;

    public Resultado() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evaluacion getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Evaluacion evaluacion) { this.evaluacion = evaluacion; }

    public Dominio getDominio() { return dominio; }
    public void setDominio(Dominio dominio) { this.dominio = dominio; }

    public Double getCoberturaPorcentaje() { return coberturaPorcentaje; }
    public void setCoberturaPorcentaje(Double coberturaPorcentaje) { this.coberturaPorcentaje = coberturaPorcentaje; }

    public boolean isEsGlobal() { return esGlobal; }
    public void setEsGlobal(boolean esGlobal) { this.esGlobal = esGlobal; }

    public NivelMadurez getNivelMadurez() { return nivelMadurez; }
    public void setNivelMadurez(NivelMadurez nivelMadurez) { this.nivelMadurez = nivelMadurez; }
}
