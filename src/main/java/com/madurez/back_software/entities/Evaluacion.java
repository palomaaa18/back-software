package com.madurez.back_software.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organizacion_id", nullable = false)
    private Organizacion organizacion;

    @ManyToOne
    @JoinColumn(name = "analista_id", nullable = false)
    private Usuario analista;

    // Jefe responsable que asignó/validó la evaluación (puede derivarse de analista.getJefe(),
    // pero lo guardamos explícito para no perder trazabilidad si el analista cambia de jefe después)
    @ManyToOne
    @JoinColumn(name = "jefe_id")
    private Usuario jefe;

    @ManyToOne
    @JoinColumn(name = "instrumento_version_id", nullable = false)
    private InstrumentoVersion instrumentoVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEvaluacion estado = EstadoEvaluacion.EN_CURSO;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio = LocalDateTime.now();

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    // Observación del jefe cuando el estado es OBSERVADA (HU0027, escenario 2)
    @Column(columnDefinition = "TEXT")
    private String observacion;

    public Evaluacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Organizacion getOrganizacion() { return organizacion; }
    public void setOrganizacion(Organizacion organizacion) { this.organizacion = organizacion; }

    public Usuario getAnalista() { return analista; }
    public void setAnalista(Usuario analista) { this.analista = analista; }

    public Usuario getJefe() { return jefe; }
    public void setJefe(Usuario jefe) { this.jefe = jefe; }

    public InstrumentoVersion getInstrumentoVersion() { return instrumentoVersion; }
    public void setInstrumentoVersion(InstrumentoVersion instrumentoVersion) { this.instrumentoVersion = instrumentoVersion; }

    public EstadoEvaluacion getEstado() { return estado; }
    public void setEstado(EstadoEvaluacion estado) { this.estado = estado; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}