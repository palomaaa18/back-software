package com.madurez.back_software.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "respuestas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"evaluacion_id", "pregunta_id"})
)
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta pregunta;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_implementacion")
    private NivelImplementacion nivelImplementacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    public Respuesta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evaluacion getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Evaluacion evaluacion) { this.evaluacion = evaluacion; }

    public Pregunta getPregunta() { return pregunta; }
    public void setPregunta(Pregunta pregunta) { this.pregunta = pregunta; }

    public NivelImplementacion getNivelImplementacion() { return nivelImplementacion; }
    public void setNivelImplementacion(NivelImplementacion nivelImplementacion) { this.nivelImplementacion = nivelImplementacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}