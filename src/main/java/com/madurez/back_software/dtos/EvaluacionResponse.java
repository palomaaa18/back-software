package com.madurez.back_software.dtos;

import java.time.LocalDateTime;

public class EvaluacionResponse {

    private Long id;
    private Long organizacionId;
    private String organizacionNombre;
    private Long analistaId;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String observacion;
    public EvaluacionResponse(Long id, Long organizacionId, String organizacionNombre, Long analistaId,
                              String estado, LocalDateTime fechaInicio, LocalDateTime fechaFin, String observacion) {
        this.id = id;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
        this.analistaId = analistaId;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observacion = observacion;
    }

    public Long getId() { return id; }
    public Long getOrganizacionId() { return organizacionId; }
    public String getOrganizacionNombre() { return organizacionNombre; }
    public Long getAnalistaId() { return analistaId; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public String getObservacion() { return observacion; }
}