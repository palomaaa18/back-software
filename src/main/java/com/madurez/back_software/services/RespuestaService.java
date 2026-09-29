package com.madurez.back_software.services;

import com.madurez.back_software.dtos.GuardarRespuestaRequest;
import com.madurez.back_software.dtos.ProgresoDominioResponse;
import com.madurez.back_software.dtos.RespuestaResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface RespuestaService {

    RespuestaResponse guardarRespuesta(Long evaluacionId, GuardarRespuestaRequest request, Usuario ejecutor);

    List<RespuestaResponse> listarRespuestas(Long evaluacionId, Usuario ejecutor);

    List<ProgresoDominioResponse> obtenerProgreso(Long evaluacionId, Usuario ejecutor);
}