package com.madurez.back_software.dtos;

public class CrearUsuarioRequest {

    private String nombre;
    private String email;
    private String password;
    private String rol; // se recibe como texto y se valida/convierte en el service
    private Long organizacionId;
    private String nombreOrganizacion;
    private String sectorOrganizacion;
    private String plataformaOrganizacion;
    public CrearUsuarioRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public Long getOrganizacionId() { return organizacionId; }
    public void setOrganizacionId(Long organizacionId) { this.organizacionId = organizacionId; }

    public String getNombreOrganizacion() { return nombreOrganizacion; }
    public void setNombreOrganizacion(String nombreOrganizacion) { this.nombreOrganizacion = nombreOrganizacion; }

    public String getSectorOrganizacion() { return sectorOrganizacion; }
    public void setSectorOrganizacion(String sectorOrganizacion) { this.sectorOrganizacion = sectorOrganizacion; }

    public String getPlataformaOrganizacion() { return plataformaOrganizacion; }
    public void setPlataformaOrganizacion(String plataformaOrganizacion) { this.plataformaOrganizacion = plataformaOrganizacion; }

}