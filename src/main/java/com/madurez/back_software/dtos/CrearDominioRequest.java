package com.madurez.back_software.dtos;

public class CrearDominioRequest {

    private String nombre;
    private String descripcion;

    public CrearDominioRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}