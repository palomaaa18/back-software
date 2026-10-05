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
import com.madurez.back_software.dtos.RecomendacionPriorizadaResponse;
import java.util.Comparator;

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
    @Override
    public List<RecomendacionPriorizadaResponse> obtenerPriorizadasPorEvaluacion(Long evaluacionId, Usuario ejecutor) {
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada"));

        if (evaluacion.getEstado() == EstadoEvaluacion.EN_CURSO) {
            throw new IllegalArgumentException("La evaluación aún no ha finalizado");
        }

        List<Respuesta> respuestas = respuestaRepository.findByEvaluacionId(evaluacionId);
        List<RecomendacionPriorizadaResponse> resultado = new java.util.ArrayList<>();

        for (Respuesta r : respuestas) {
            NivelImplementacion nivel = r.getNivelImplementacion();

            if (nivel != NivelImplementacion.NO_EXISTE && nivel != NivelImplementacion.EXISTE_PARCIALMENTE) {
                continue;
            }

            String riesgo = nivel == NivelImplementacion.NO_EXISTE ? "ALTO" : "MEDIO";

            recomendacionRepository.findByPreguntaIdAndNivel(r.getPregunta().getId(), nivel).ifPresent(rec -> {
                Pregunta p = r.getPregunta();
                // Tomamos el primer control de la pregunta para el desempate alfabético (HU023 esc.2)
                String anexo = p.getControles().stream()
                        .map(com.madurez.back_software.entities.Control::getAnexoA)
                        .min(String::compareTo)
                        .orElse("");

                resultado.add(new RecomendacionPriorizadaResponse(
                        p.getDominio().getNombre(),
                        p.getDominio().getPesoRelativo(),
                        riesgo,
                        anexo,
                        p.getTexto(),
                        rec.getDescripcion()
                ));
            });
        }

        resultado.sort(
                Comparator.comparing(RecomendacionPriorizadaResponse::getPesoDominio).reversed()
                        .thenComparing(x -> x.getNivelRiesgo().equals("ALTO") ? 0 : 1)
                        .thenComparing(RecomendacionPriorizadaResponse::getControlAnexoA)
        );

        return resultado;
    }
}