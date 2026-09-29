package com.madurez.back_software.dtos;

import java.time.LocalDateTime;

public class AuditoriaTecnicaResponse {

    private Long id;
    private String usuarioNombre;
    private String entidadAfectada;
    private String accion;
    private String detalle;
    private LocalDateTime fecha;

    public AuditoriaTecnicaResponse(Long id, String usuarioNombre, String entidadAfectada,
                                    String accion, String detalle, LocalDateTime fecha) {
        this.id = id;
        this.usuarioNombre = usuarioNombre;
        this.entidadAfectada = entidadAfectada;
        this.accion = accion;
        this.detalle = detalle;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public String getEntidadAfectada() { return entidadAfectada; }
    public String getAccion() { return accion; }
    public String getDetalle() { return detalle; }
    public LocalDateTime getFecha() { return fecha; }
}