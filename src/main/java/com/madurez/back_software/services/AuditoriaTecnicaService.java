package com.madurez.back_software.services;

import com.madurez.back_software.dtos.AuditoriaTecnicaResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface AuditoriaTecnicaService {

    void registrar(Usuario usuario, String entidadAfectada, String accion, String detalle);

    List<AuditoriaTecnicaResponse> listarTodo();

    List<AuditoriaTecnicaResponse> listarPorEntidad(String entidadAfectada);
}