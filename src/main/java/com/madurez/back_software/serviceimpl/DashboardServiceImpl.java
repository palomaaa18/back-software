package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.ComparativaResponse;
import com.madurez.back_software.dtos.DashboardResponse;
import com.madurez.back_software.dtos.HistoricoItem;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.*;
import com.madurez.back_software.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrganizacionRepository organizacionRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Override
    public DashboardResponse obtenerDashboard(Long organizacionId, Usuario ejecutor) {
        // HU0029/30: pensado para Gerente general, pero el Jefe y el Administrador
        // también deberían poder verlo para revisión interna.
        if (ejecutor.getRol() == Rol.ANALISTA_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Tu rol no tiene acceso al dashboard ejecutivo");
        }

        Organizacion organizacion = organizacionRepository.findById(organizacionId)
                .orElseThrow(() -> new IllegalArgumentException("Organización no encontrada"));

        List<Evaluacion> evaluaciones = evaluacionRepository
                .findByOrganizacionIdAndFechaFinIsNotNullOrderByFechaFinDesc(organizacionId);

        // HU0029 esc.2
        if (evaluaciones.isEmpty()) {
            throw new IllegalArgumentException(
                    "Aún no existen resultados disponibles para esta organización");
        }

        Evaluacion actual = evaluaciones.get(0);
        List<Resultado> resultadosActual = resultadoRepository.findByEvaluacionId(actual.getId());

        Resultado globalActual = resultadosActual.stream()
                .filter(Resultado::isEsGlobal)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el resultado global de la evaluación"));

        List<DashboardResponse.DominioItem> porDominio = resultadosActual.stream()
                .filter(r -> !r.isEsGlobal())
                .map(r -> new DashboardResponse.DominioItem(
                        r.getDominio().getId(), r.getDominio().getNombre(),
                        r.getCoberturaPorcentaje(), r.getDominio().getPesoRelativo()))
                .collect(Collectors.toList());

        // HU0030 esc.2: comparar con la evaluación anterior (si existe)
        String tendencia = "SIN_COMPARATIVA";
        if (evaluaciones.size() > 1) {
            Evaluacion anterior = evaluaciones.get(1);
            List<Resultado> resultadosAnterior = resultadoRepository.findByEvaluacionId(anterior.getId());

            Resultado globalAnterior = resultadosAnterior.stream()
                    .filter(Resultado::isEsGlobal)
                    .findFirst()
                    .orElse(null);

            if (globalAnterior != null && globalAnterior.getNivelMadurez() != null
                    && globalActual.getNivelMadurez() != null) {
                double rangoActual = globalActual.getNivelMadurez().getRangoMin();
                double rangoAnterior = globalAnterior.getNivelMadurez().getRangoMin();

                if (rangoActual > rangoAnterior) tendencia = "SUBIO";
                else if (rangoActual < rangoAnterior) tendencia = "BAJO";
                else tendencia = "MANTUVO";
            }
        }

        String nombreNivel = globalActual.getNivelMadurez() != null
                ? globalActual.getNivelMadurez().getNombre() : "Sin clasificar";

        return new DashboardResponse(
                organizacion.getId(), organizacion.getNombre(), actual.getFechaFin(),
                globalActual.getCoberturaPorcentaje(), nombreNivel, tendencia, porDominio
        );
    }
    @Override
    public ComparativaResponse obtenerComparativa(Long organizacionId, Usuario ejecutor) {
        if (ejecutor.getRol() == Rol.ANALISTA_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Tu rol no tiene acceso a la vista comparativa");
        }

        List<Evaluacion> evaluaciones = evaluacionRepository
                .findByOrganizacionIdAndFechaFinIsNotNullOrderByFechaFinDesc(organizacionId);

        if (evaluaciones.size() < 2) {
            throw new IllegalArgumentException(
                    "Se necesitan al menos dos evaluaciones finalizadas para comparar");
        }

        Evaluacion actual = evaluaciones.get(0);
        Evaluacion anterior = evaluaciones.get(1);

        List<Resultado> resultadosActual = resultadoRepository.findByEvaluacionId(actual.getId());
        List<Resultado> resultadosAnterior = resultadoRepository.findByEvaluacionId(anterior.getId());

        Resultado globalActual = resultadosActual.stream().filter(Resultado::isEsGlobal).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Resultado global no encontrado (actual)"));
        Resultado globalAnterior = resultadosAnterior.stream().filter(Resultado::isEsGlobal).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Resultado global no encontrado (anterior)"));

        String tendenciaGlobal;
        if (globalActual.getCoberturaPorcentaje() > globalAnterior.getCoberturaPorcentaje()) tendenciaGlobal = "MEJORO";
        else if (globalActual.getCoberturaPorcentaje() < globalAnterior.getCoberturaPorcentaje()) tendenciaGlobal = "EMPEORO";
        else tendenciaGlobal = "IGUAL";

        // Mapear resultados anteriores por dominio para cruzarlos con los actuales
        var anteriorPorDominio = resultadosAnterior.stream()
                .filter(r -> !r.isEsGlobal())
                .collect(Collectors.toMap(r -> r.getDominio().getId(), r -> r));

        List<ComparativaResponse.DeltaDominio> deltas = resultadosActual.stream()
                .filter(r -> !r.isEsGlobal())
                .map(r -> {
                    Resultado rAnterior = anteriorPorDominio.get(r.getDominio().getId());
                    double coberturaAnterior = rAnterior != null ? rAnterior.getCoberturaPorcentaje() : 0.0;
                    double delta = r.getCoberturaPorcentaje() - coberturaAnterior;
                    return new ComparativaResponse.DeltaDominio(
                            r.getDominio().getNombre(), r.getCoberturaPorcentaje(), coberturaAnterior, delta);
                })
                .collect(Collectors.toList());

        return new ComparativaResponse(
                actual.getFechaFin(), anterior.getFechaFin(),
                globalActual.getCoberturaPorcentaje(), globalAnterior.getCoberturaPorcentaje(),
                globalActual.getNivelMadurez() != null ? globalActual.getNivelMadurez().getNombre() : "Sin clasificar",
                globalAnterior.getNivelMadurez() != null ? globalAnterior.getNivelMadurez().getNombre() : "Sin clasificar",
                tendenciaGlobal, deltas
        );
    }

    @Override
    public List<HistoricoItem> obtenerHistorico(Long organizacionId, Usuario ejecutor) {
        if (ejecutor.getRol() == Rol.ANALISTA_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Tu rol no tiene acceso al histórico");
        }

        List<Evaluacion> evaluaciones = evaluacionRepository
                .findByOrganizacionIdAndFechaFinIsNotNullOrderByFechaFinDesc(organizacionId);

        // HU0035 esc.2: si hay una sola, se muestra igual sin error (esta lógica ya lo permite,
        // simplemente devuelve una lista de un elemento)
        return evaluaciones.stream().map(e -> {
            Resultado global = resultadoRepository.findByEvaluacionId(e.getId()).stream()
                    .filter(Resultado::isEsGlobal)
                    .findFirst()
                    .orElse(null);

            return new HistoricoItem(
                    e.getId(), e.getFechaFin(),
                    global != null ? global.getCoberturaPorcentaje() : null,
                    global != null && global.getNivelMadurez() != null ? global.getNivelMadurez().getNombre() : "Sin clasificar",
                    e.getEstado().name()
            );
        }).collect(Collectors.toList());
    }
    @Override
    public List<HistoricoItem> obtenerHistoricoFiltrado(Long organizacionId, LocalDateTime desde,
                                                        LocalDateTime hasta, Usuario ejecutor) {
        if (ejecutor.getRol() == Rol.ANALISTA_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Tu rol no tiene acceso al histórico");
        }

        List<Evaluacion> evaluaciones = evaluacionRepository
                .findByOrganizacionIdAndFechaFinBetweenOrderByFechaFinDesc(organizacionId, desde, hasta);

        // HU0037 esc.2: si no hay coincidencias, se devuelve vacío (el controller
        // decide si eso se traduce en un mensaje o simplemente una lista vacía)
        return evaluaciones.stream().map(e -> {
            Resultado global = resultadoRepository.findByEvaluacionId(e.getId()).stream()
                    .filter(Resultado::isEsGlobal)
                    .findFirst()
                    .orElse(null);

            return new HistoricoItem(
                    e.getId(), e.getFechaFin(),
                    global != null ? global.getCoberturaPorcentaje() : null,
                    global != null && global.getNivelMadurez() != null ? global.getNivelMadurez().getNombre() : "Sin clasificar",
                    e.getEstado().name()
            );
        }).collect(Collectors.toList());
    }
}