package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.EvaluacionResponse;
import com.madurez.back_software.dtos.IniciarEvaluacionRequest;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.*;
import com.madurez.back_software.services.EvaluacionService;
import com.madurez.back_software.services.ResultadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluacionServiceImpl implements EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private OrganizacionRepository organizacionRepository;

    @Autowired
    private InstrumentoVersionRepository instrumentoVersionRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private ResultadoService resultadoService;

    @Override
    public EvaluacionResponse iniciarEvaluacion(Usuario analista) {
        if (analista.getRol() != Rol.ANALISTA_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Solo un Analista de ciberseguridad puede iniciar una evaluación");
        }

        if (analista.getOrganizacion() == null) {
            throw new IllegalArgumentException(
                    "Tu cuenta no tiene una organización asignada. Contacta a tu Jefe de ciberseguridad");
        }

        InstrumentoVersion versionVigente = instrumentoVersionRepository.findByVigenteTrue()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No hay una versión vigente del instrumento configurada"));

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setOrganizacion(analista.getOrganizacion());
        evaluacion.setAnalista(analista);
        evaluacion.setJefe(analista.getJefe());
        evaluacion.setInstrumentoVersion(versionVigente);
        evaluacion.setEstado(EstadoEvaluacion.EN_CURSO);

        Evaluacion guardada = evaluacionRepository.save(evaluacion);

        return mapearAResponse(guardada);
    }

    @Override
    public List<EvaluacionResponse> listarMisEvaluaciones(Usuario analista) {
        return evaluacionRepository.findByAnalistaId(analista.getId()).stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluacionResponse obtenerEvaluacion(Long evaluacionId, Usuario ejecutor) {
        Evaluacion evaluacion = buscarYValidarAcceso(evaluacionId, ejecutor);
        return mapearAResponse(evaluacion);
    }

    @Override
    public void finalizarEvaluacion(Long evaluacionId, Usuario ejecutor) {
        Evaluacion evaluacion = buscarYValidarAcceso(evaluacionId, ejecutor);

        if (evaluacion.getEstado() != EstadoEvaluacion.EN_CURSO) {
            throw new IllegalArgumentException("La evaluación no está en curso");
        }

        long totalPreguntas = preguntaRepository
                .findByInstrumentoVersionId(evaluacion.getInstrumentoVersion().getId()).size();
        long respondidas = respuestaRepository.countByEvaluacionId(evaluacion.getId());

        // HU0021 esc.2 / HU0022: no permitir finalizar con preguntas pendientes
        if (respondidas < totalPreguntas) {
            throw new IllegalArgumentException(
                    "No se puede finalizar: faltan " + (totalPreguntas - respondidas) + " pregunta(s) por responder");
        }

        evaluacion.setEstado(EstadoEvaluacion.FINALIZADA);
        evaluacion.setFechaFin(LocalDateTime.now());
        evaluacionRepository.save(evaluacion);
        resultadoService.calcularYGuardarResultado(evaluacion.getId());
    }

    // Válida que quien consulta/modifica la evaluación sea el analista dueño
    // o su jefe directo
    private Evaluacion buscarYValidarAcceso(Long evaluacionId, Usuario ejecutor) {
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada"));

        boolean esDueño = evaluacion.getAnalista().getId().equals(ejecutor.getId());
        boolean esSuJefe = evaluacion.getJefe() != null && evaluacion.getJefe().getId().equals(ejecutor.getId());
        boolean esAdmin = ejecutor.getRol() == Rol.ADMINISTRADOR;

        if (!esDueño && !esSuJefe && !esAdmin) {
            throw new IllegalArgumentException("No tiene acceso a esta evaluación");
        }

        return evaluacion;
    }

    private EvaluacionResponse mapearAResponse(Evaluacion e) {
        return new EvaluacionResponse(
                e.getId(), e.getOrganizacion().getId(), e.getOrganizacion().getNombre(),
                e.getAnalista().getId(), e.getEstado().name(), e.getFechaInicio(), e.getFechaFin(),
                e.getObservacion()
        );
    }

    @Override
    public EvaluacionResponse validarEvaluacion(Long evaluacionId, Usuario jefe) {
        if (jefe.getRol() != Rol.JEFE_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Solo el Jefe de ciberseguridad puede validar evaluaciones");
        }

        Evaluacion evaluacion = buscarYValidarAcceso(evaluacionId, jefe);

        if (evaluacion.getEstado() != EstadoEvaluacion.FINALIZADA) {
            throw new IllegalArgumentException("Solo se pueden validar evaluaciones finalizadas");
        }

        evaluacion.setEstado(EstadoEvaluacion.VALIDADA);
        evaluacion.setObservacion(null);
        Evaluacion guardada = evaluacionRepository.save(evaluacion);

        return mapearAResponse(guardada);
    }

    @Override
    public EvaluacionResponse observarEvaluacion(Long evaluacionId, String observacion, Usuario jefe) {
        if (jefe.getRol() != Rol.JEFE_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Solo el Jefe de ciberseguridad puede observar evaluaciones");
        }

        if (observacion == null || observacion.isBlank()) {
            throw new IllegalArgumentException("Debe registrar el motivo de la observación");
        }

        Evaluacion evaluacion = buscarYValidarAcceso(evaluacionId, jefe);

        if (evaluacion.getEstado() != EstadoEvaluacion.FINALIZADA) {
            throw new IllegalArgumentException("Solo se pueden observar evaluaciones finalizadas");
        }

        evaluacion.setEstado(EstadoEvaluacion.OBSERVADA);
        evaluacion.setObservacion(observacion);
        Evaluacion guardada = evaluacionRepository.save(evaluacion);

        return mapearAResponse(guardada);
    }

    @Override
    public EvaluacionResponse iniciarReevaluacion(Usuario analista) {
        if (analista.getRol() != Rol.ANALISTA_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Solo un Analista de ciberseguridad puede iniciar una reevaluación");
        }

        if (analista.getOrganizacion() == null) {
            throw new IllegalArgumentException("Tu cuenta no tiene una organización asignada");
        }

        Long organizacionId = analista.getOrganizacion().getId();

        boolean tieneEvaluacionPrevia = !evaluacionRepository
                .findByOrganizacionIdAndFechaFinIsNotNullOrderByFechaFinDesc(organizacionId).isEmpty();

        if (!tieneEvaluacionPrevia) {
            throw new IllegalArgumentException(
                    "No se puede reevaluar: la organización no cuenta con ninguna evaluación finalizada previa");
        }

        InstrumentoVersion versionVigente = instrumentoVersionRepository.findByVigenteTrue()
                .orElseThrow(() -> new IllegalArgumentException("No hay una versión vigente del instrumento configurada"));

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setOrganizacion(analista.getOrganizacion());
        evaluacion.setAnalista(analista);
        evaluacion.setJefe(analista.getJefe());
        evaluacion.setInstrumentoVersion(versionVigente);
        evaluacion.setEstado(EstadoEvaluacion.EN_CURSO);

        Evaluacion guardada = evaluacionRepository.save(evaluacion);

        return mapearAResponse(guardada);
    }

    @Override
    public List<EvaluacionResponse> listarEvaluacionesEquipo(Usuario jefe) {
        if (jefe.getRol() != Rol.JEFE_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Solo el Jefe de ciberseguridad puede ver las evaluaciones de su equipo");
        }

        return evaluacionRepository.findByJefeId(jefe.getId()).stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }
}