package com.madurez.back_software.dtos;

import java.time.LocalDateTime;

public class VersionInstrumentoResponse {

    private Long id;
    private Integer numeroVersion;
    private LocalDateTime fechaPublicacion;
    private boolean vigente;
    private String nota;
    private long totalDominios;
    private long totalControles;
    private long totalPreguntas;

    public VersionInstrumentoResponse(Long id, Integer numeroVersion, LocalDateTime fechaPublicacion, boolean vigente,
                                      String nota, long totalDominios, long totalControles, long totalPreguntas) {
        this.id = id;
        this.numeroVersion = numeroVersion;
        this.fechaPublicacion = fechaPublicacion;
        this.vigente = vigente;
        this.nota = nota;
        this.totalDominios = totalDominios;
        this.totalControles = totalControles;
        this.totalPreguntas = totalPreguntas;
    }

    public Long getId() { return id; }
    public Integer getNumeroVersion() { return numeroVersion; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public boolean isVigente() { return vigente; }
    public String getNota() { return nota; }
    public long getTotalDominios() { return totalDominios; }
    public long getTotalControles() { return totalControles; }
    public long getTotalPreguntas() { return totalPreguntas; }
}
