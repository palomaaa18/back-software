package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.CrearRecomendacionRequest;
import com.madurez.back_software.dtos.RecomendacionResponse;
import com.madurez.back_software.entities.NivelImplementacion;
import com.madurez.back_software.entities.Pregunta;
import com.madurez.back_software.entities.Recomendacion;
import com.madurez.back_software.entities.Rol;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.repositories.PreguntaRepository;
import com.madurez.back_software.repositories.RecomendacionRepository;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import com.madurez.back_software.services.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.madurez.back_software.dtos.RecomendacionSugeridaResponse;
import com.madurez.back_software.entities.Evaluacion;
import com.madurez.back_software.entities.EstadoEvaluacion;
import com.madurez.back_software.entities.NivelImplementacion;
import com.madurez.back_software.entities.Respuesta;
import com.madurez.back_software.repositories.EvaluacionRepository;
import com.madurez.back_software.repositories.RespuestaRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecomendacionServiceImpl implements RecomendacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private RecomendacionRepository recomendacionRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Override
    public RecomendacionResponse crearRecomendacion(CrearRecomendacionRequest request, Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede registrar recomendaciones");
        }

        if (request.getPreguntaId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una pregunta asociada");
        }

        Pregunta pregunta = preguntaRepository.findById(request.getPreguntaId())
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada"));

        NivelImplementacion nivel;
        try {
            nivel = NivelImplementacion.valueOf(request.getNivel());
        } catch (Exception e) {
            throw new IllegalArgumentException("Nivel inválido: " + request.getNivel());
        }

        Recomendacion recomendacion = recomendacionRepository
                .findByPreguntaIdAndNivel(pregunta.getId(), nivel)
                .orElseGet(() -> {
                    Recomendacion r = new Recomendacion();
                    r.setPregunta(pregunta);
                    r.setNivel(nivel);
                    return r;
                });

        recomendacion.setDescripcion(request.getDescripcion());

        Recomendacion guardada = recomendacionRepository.save(recomendacion);
        auditoriaTecnicaService.registrar(ejecutor, "Recomendacion", "CREAR",
                "Recomendación (" + nivel.name() + ") para pregunta: " + pregunta.getTexto());
        return new RecomendacionResponse(
                guardada.getId(), guardada.getDescripcion(),
                pregunta.getId(), pregunta.getTexto(), nivel.name()
        );
    }

    @Override
    public List<RecomendacionResponse> listarPorPregunta(Long preguntaId) {
        return recomendacionRepository.findByPreguntaId(preguntaId).stream()
                .map(r -> new RecomendacionResponse(
                        r.getId(), r.getDescripcion(), r.getPregunta().getId(),
                        r.getPregunta().getTexto(), r.getNivel().name()))
                .collect(Collectors.toList());
    }
    @Autowired
    private AuditoriaTecnicaService auditoriaTecnicaService;
    @Override
    public List<RecomendacionSugeridaResponse> obtenerSugeridasPorEvaluacion(Long evaluacionId, Usuario ejecutor) {
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada"));

        if (evaluacion.getEstado() == EstadoEvaluacion.EN_CURSO) {
            throw new IllegalArgumentException("La evaluación aún no ha finalizado");
        }

        List<Respuesta> respuestas = respuestaRepository.findByEvaluacionId(evaluacionId);

        List<RecomendacionSugeridaResponse> sugeridas = new java.util.ArrayList<>();

        for (Respuesta r : respuestas) {
            NivelImplementacion nivel = r.getNivelImplementacion();

            // HU0025: solo se sugieren recomendaciones para NO_EXISTE o EXISTE_PARCIALMENTE
            if (nivel != NivelImplementacion.NO_EXISTE && nivel != NivelImplementacion.EXISTE_PARCIALMENTE) {
                continue;
            }

            recomendacionRepository.findByPreguntaIdAndNivel(r.getPregunta().getId(), nivel)
                    .ifPresent(rec -> sugeridas.add(new RecomendacionSugeridaResponse(
                            r.getPregunta().getId(),
                            r.getPregunta().getTexto(),
                            r.getPregunta().getDominio().getNombre(),
                            nivel.name(),
                            rec.getDescripcion()
                    )));
        }

        return sugeridas;
    }
}