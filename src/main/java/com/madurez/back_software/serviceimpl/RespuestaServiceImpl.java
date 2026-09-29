package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.GuardarRespuestaRequest;
import com.madurez.back_software.dtos.ProgresoDominioResponse;
import com.madurez.back_software.dtos.RespuestaResponse;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.*;
import com.madurez.back_software.services.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RespuestaServiceImpl implements RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private DominioRepository dominioRepository;

    @Override
    public RespuestaResponse guardarRespuesta(Long evaluacionId, GuardarRespuestaRequest request, Usuario ejecutor) {
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada"));

        if (!evaluacion.getAnalista().getId().equals(ejecutor.getId())) {
            throw new IllegalArgumentException("Solo el analista responsable puede responder esta evaluación");
        }

        if (evaluacion.getEstado() != EstadoEvaluacion.EN_CURSO) {
            throw new IllegalArgumentException("La evaluación no está en curso, no se puede modificar");
        }

        Pregunta pregunta = preguntaRepository.findById(request.getPreguntaId())
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada"));

        NivelImplementacion nivel;
        try {
            nivel = NivelImplementacion.valueOf(request.getNivelImplementacion());
        } catch (Exception e) {
            throw new IllegalArgumentException("Nivel de implementación inválido");
        }

        // HU0017 esc.3: si ya existe respuesta para esta pregunta, se reemplaza
        Respuesta respuesta = respuestaRepository
                .findByEvaluacionIdAndPreguntaId(evaluacionId, request.getPreguntaId())
                .orElseGet(() -> {
                    Respuesta r = new Respuesta();
                    r.setEvaluacion(evaluacion);
                    r.setPregunta(pregunta);
                    return r;
                });

        respuesta.setNivelImplementacion(nivel);
        respuesta.setComentario(request.getComentario());

        Respuesta guardada = respuestaRepository.save(respuesta);

        return new RespuestaResponse(
                guardada.getId(), pregunta.getId(), pregunta.getTexto(),
                guardada.getNivelImplementacion().name(), guardada.getComentario()
        );
    }

    @Override
    public List<RespuestaResponse> listarRespuestas(Long evaluacionId, Usuario ejecutor) {
        return respuestaRepository.findByEvaluacionId(evaluacionId).stream()
                .map(r -> new RespuestaResponse(
                        r.getId(), r.getPregunta().getId(), r.getPregunta().getTexto(),
                        r.getNivelImplementacion().name(), r.getComentario()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgresoDominioResponse> obtenerProgreso(Long evaluacionId, Usuario ejecutor) {
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada"));

        List<Dominio> dominios = dominioRepository
                .findByInstrumentoVersionId(evaluacion.getInstrumentoVersion().getId());

        List<Respuesta> respuestas = respuestaRepository.findByEvaluacionId(evaluacionId);

        // Contamos respuestas ya dadas, agrupadas por dominio (vía pregunta -> dominio)
        Map<Long, Long> respondidasPorDominio = respuestas.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getPregunta().getDominio().getId(), Collectors.counting()));

        return dominios.stream().map(d -> {
            int total = preguntaRepository.findByDominioId(d.getId()).size();
            int respondidas = respondidasPorDominio.getOrDefault(d.getId(), 0L).intValue();
            return new ProgresoDominioResponse(d.getId(), d.getNombre(), total, respondidas);
        }).collect(Collectors.toList());
    }
}