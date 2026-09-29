package com.madurez.back_software.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "controles")
public class Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Norma norma;

    @Column(name = "anexo_a", nullable = false)
    private String anexoA;

    @ManyToOne
    @JoinColumn(name = "dominio_id", nullable = false)
    private Dominio dominio;

    @ManyToOne
    @JoinColumn(name = "instrumento_version_id", nullable = false)
    private InstrumentoVersion instrumentoVersion;

    public Control() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Norma getNorma() { return norma; }
    public void setNorma(Norma norma) { this.norma = norma; }

    public String getAnexoA() { return anexoA; }
    public void setAnexoA(String anexoA) { this.anexoA = anexoA; }

    public Dominio getDominio() { return dominio; }
    public void setDominio(Dominio dominio) { this.dominio = dominio; }

    public InstrumentoVersion getInstrumentoVersion() { return instrumentoVersion; }
    public void setInstrumentoVersion(InstrumentoVersion instrumentoVersion) { this.instrumentoVersion = instrumentoVersion; }
}