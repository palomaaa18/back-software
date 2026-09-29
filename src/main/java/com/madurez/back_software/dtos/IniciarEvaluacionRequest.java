package com.madurez.back_software.dtos;

public class IniciarEvaluacionRequest {

    // Si se manda organizacionId, se usa una existente.
    // Si no, se crea una nueva con estos datos.
    private Long organizacionId;
    private String nombreOrganizacion;
    private String sector;
    private String plataformaTextToSql;

    public Long getOrganizacionId() { return organizacionId; }
    public void setOrganizacionId(Long organizacionId) { this.organizacionId = organizacionId; }

    public String getNombreOrganizacion() { return nombreOrganizacion; }
    public void setNombreOrganizacion(String nombreOrganizacion) { this.nombreOrganizacion = nombreOrganizacion; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getPlataformaTextToSql() { return plataformaTextToSql; }
    public void setPlataformaTextToSql(String plataformaTextToSql) { this.plataformaTextToSql = plataformaTextToSql; }
}