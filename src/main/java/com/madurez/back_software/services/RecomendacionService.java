package com.madurez.back_software.services;

import com.madurez.back_software.dtos.CrearRecomendacionRequest;
import com.madurez.back_software.dtos.RecomendacionPriorizadaResponse;
import com.madurez.back_software.dtos.RecomendacionResponse;
import com.madurez.back_software.dtos.RecomendacionSugeridaResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface RecomendacionService {

    RecomendacionResponse crearRecomendacion(CrearRecomendacionRequest request, Usuario ejecutor);

    List<RecomendacionResponse> listarPorPregunta(Long preguntaId);
    List<RecomendacionSugeridaResponse> obtenerSugeridasPorEvaluacion(Long evaluacionId, Usuario ejecutor);
    List<RecomendacionPriorizadaResponse> obtenerPriorizadasPorEvaluacion(Long evaluacionId, Usuario ejecutor);


}