package com.madurez.back_software.services;

import com.madurez.back_software.dtos.EvaluacionResponse;
import com.madurez.back_software.dtos.IniciarEvaluacionRequest;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface EvaluacionService {

    EvaluacionResponse iniciarEvaluacion(IniciarEvaluacionRequest request, Usuario analista);

    List<EvaluacionResponse> listarMisEvaluaciones(Usuario analista);

    EvaluacionResponse obtenerEvaluacion(Long evaluacionId, Usuario ejecutor);

    void finalizarEvaluacion(Long evaluacionId, Usuario ejecutor);
    EvaluacionResponse validarEvaluacion(Long evaluacionId, Usuario jefe);

    EvaluacionResponse observarEvaluacion(Long evaluacionId, String observacion, Usuario jefe);
    EvaluacionResponse iniciarReevaluacion(Long organizacionId, Usuario analista);
    List<EvaluacionResponse> listarEvaluacionesEquipo(Usuario jefe);
}