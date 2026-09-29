package com.madurez.back_software.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String texto;

    @ManyToOne
    @JoinColumn(name = "dominio_id", nullable = false)
    private Dominio dominio;

    @ManyToOne
    @JoinColumn(name = "instrumento_version_id", nullable = false)
    private InstrumentoVersion instrumentoVersion;

    @ManyToMany
    @JoinTable(
            name = "pregunta_control",
            joinColumns = @JoinColumn(name = "pregunta_id"),
            inverseJoinColumns = @JoinColumn(name = "control_id")
    )
    private Set<Control> controles = new HashSet<>();

    public Pregunta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Dominio getDominio() { return dominio; }
    public void setDominio(Dominio dominio) { this.dominio = dominio; }

    public InstrumentoVersion getInstrumentoVersion() { return instrumentoVersion; }
    public void setInstrumentoVersion(InstrumentoVersion instrumentoVersion) { this.instrumentoVersion = instrumentoVersion; }

    public Set<Control> getControles() { return controles; }
    public void setControles(Set<Control> controles) { this.controles = controles; }
}