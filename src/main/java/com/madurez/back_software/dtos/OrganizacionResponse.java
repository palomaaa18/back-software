package com.madurez.back_software.dtos;

public class OrganizacionResponse {

    private Long id;
    private String nombre;
    private String sector;

    public OrganizacionResponse(Long id, String nombre, String sector) {
        this.id = id;
        this.nombre = nombre;
        this.sector = sector;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getSector() { return sector; }
}