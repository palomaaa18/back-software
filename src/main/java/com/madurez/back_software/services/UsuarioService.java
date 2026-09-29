package com.madurez.back_software.services;

import com.madurez.back_software.dtos.CrearUsuarioRequest;
import com.madurez.back_software.dtos.UsuarioResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse crearUsuario(CrearUsuarioRequest request, Usuario creador);

    void desactivarUsuario(Long usuarioId, Usuario ejecutor);
    List<UsuarioResponse> listarUsuarios(Usuario ejecutor);
}