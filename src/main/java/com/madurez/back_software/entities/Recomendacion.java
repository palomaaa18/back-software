package com.madurez.back_software.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "recomendaciones",
        uniqueConstraints = @UniqueConstraint(columnNames = {"pregunta_id", "nivel"})
)
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta pregunta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelImplementacion nivel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    public Recomendacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pregunta getPregunta() { return pregunta; }
    public void setPregunta(Pregunta pregunta) { this.pregunta = pregunta; }

    public NivelImplementacion getNivel() { return nivel; }
    public void setNivel(NivelImplementacion nivel) { this.nivel = nivel; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}