package com.madurez.back_software.services;

import com.madurez.back_software.dtos.CrearPreguntaRequest;
import com.madurez.back_software.dtos.PreguntaResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface PreguntaService {

    PreguntaResponse crearPregunta(CrearPreguntaRequest request, Usuario ejecutor);

    List<PreguntaResponse> listarPreguntas();

    List<PreguntaResponse> listarPorDominio(Long dominioId);
}