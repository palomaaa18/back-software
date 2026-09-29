package com.madurez.back_software.services;

import com.madurez.back_software.dtos.ControlResponse;
import com.madurez.back_software.dtos.CrearControlRequest;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface ControlService {

    ControlResponse crearControl(CrearControlRequest request, Usuario ejecutor);

    List<ControlResponse> listarControles();
}
