package com.madurez.back_software.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "instrumento_versiones")
public class InstrumentoVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_version", nullable = false)
    private Integer numeroVersion;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion = LocalDateTime.now();

    @Column(nullable = false)
    private boolean vigente = true;

    public InstrumentoVersion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getNumeroVersion() { return numeroVersion; }
    public void setNumeroVersion(Integer numeroVersion) { this.numeroVersion = numeroVersion; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public boolean isVigente() { return vigente; }
    public void setVigente(boolean vigente) { this.vigente = vigente; }
}