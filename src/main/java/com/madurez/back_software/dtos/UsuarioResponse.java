package com.madurez.back_software.dtos;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private String estado;
    private Long jefeId;
    private Long organizacionId;
    private String organizacionNombre;

    public UsuarioResponse(Long id, String nombre, String email, String rol, String estado, Long jefeId,
                           Long organizacionId, String organizacionNombre) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
        this.jefeId = jefeId;
        this.organizacionId = organizacionId;
        this.organizacionNombre = organizacionNombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getEstado() { return estado; }
    public Long getJefeId() { return jefeId; }
    public Long getOrganizacionId() { return organizacionId; }
    public String getOrganizacionNombre() { return organizacionNombre; }
}