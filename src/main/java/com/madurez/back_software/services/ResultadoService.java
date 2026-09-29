package com.madurez.back_software.services;

import com.madurez.back_software.dtos.ResultadoResponse;
import com.madurez.back_software.entities.Usuario;

public interface ResultadoService {

    void calcularYGuardarResultado(Long evaluacionId);

    ResultadoResponse obtenerResultado(Long evaluacionId, Usuario ejecutor);
}