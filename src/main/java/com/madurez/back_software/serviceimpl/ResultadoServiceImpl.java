package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.ResultadoResponse;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.*;
import com.madurez.back_software.services.ResultadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultadoServiceImpl implements ResultadoService {

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private NivelMadurezRepository nivelMadurezRepository;

    // Conversión de nivel de implementación a valor numérico de cobertura
    private double valorCobertura(NivelImplementacion nivel) {
        return switch (nivel) {
            case NO_EXISTE -> 0.0;
            case EXISTE_PARCIALMENTE -> 33.0;
            case EXISTE_NO_FORMALIZADO -> 66.0;
            case IMPLEMENTADO -> 100.0;
        };
    }

    @Override
    public void calcularYGuardarResultado(Long evaluacionId) {
        Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada"));

        List<Respuesta> respuestas = respuestaRepository.findByEvaluacionId(evaluacionId);
        List<Dominio> dominios = dominioRepository
                .findByInstrumentoVersionId(evaluacion.getInstrumentoVersion().getId());

        // Agrupar respuestas por dominio (vía pregunta -> dominio)
        Map<Long, List<Respuesta>> respuestasPorDominio = respuestas.stream()
                .collect(Collectors.groupingBy(r -> r.getPregunta().getDominio().getId()));

        // Limpiar resultados previos por si acaso (evita duplicar si se recalcula)
        resultadoRepository.deleteByEvaluacionId(evaluacionId);

        double coberturaGlobal = 0.0;
        List<Resultado> resultadosDominio = new ArrayList<>();

        for (Dominio dominio : dominios) {
            List<Respuesta> respuestasDominio = respuestasPorDominio.getOrDefault(dominio.getId(), List.of());

            double coberturaDominio;
            if (respuestasDominio.isEmpty()) {
                coberturaDominio = 0.0;
            } else {
                coberturaDominio = respuestasDominio.stream()
                        .mapToDouble(r -> valorCobertura(r.getNivelImplementacion()))
                        .average()
                        .orElse(0.0);
            }

            Resultado resultadoDominio = new Resultado();
            resultadoDominio.setEvaluacion(evaluacion);
            resultadoDominio.setDominio(dominio);
            resultadoDominio.setCoberturaPorcentaje(coberturaDominio);
            resultadoDominio.setEsGlobal(false);
            resultadosDominio.add(resultadoDominio);

            // Suma ponderada para la cobertura global
            coberturaGlobal += coberturaDominio * (dominio.getPesoRelativo() / 100.0);
        }

        resultadoRepository.saveAll(resultadosDominio);

        // Determinar nivel de madurez según la cobertura global
        NivelMadurez nivelMadurez = nivelMadurezRepository
                .findByRangoMinLessThanEqualAndRangoMaxGreaterThanEqual(coberturaGlobal, coberturaGlobal)
                .orElse(null);

        Resultado resultadoGlobal = new Resultado();
        resultadoGlobal.setEvaluacion(evaluacion);
        resultadoGlobal.setDominio(null);
        resultadoGlobal.setCoberturaPorcentaje(coberturaGlobal);
        resultadoGlobal.setEsGlobal(true);
        resultadoGlobal.setNivelMadurez(nivelMadurez);

        resultadoRepository.save(resultadoGlobal);
    }

    @Override
    public ResultadoResponse obtenerResultado(Long evaluacionId, Usuario ejecutor) {
        List<Resultado> resultados = resultadoRepository.findByEvaluacionId(evaluacionId);

        if (resultados.isEmpty()) {
            throw new IllegalArgumentException("Aún no hay resultados para esta evaluación");
        }

        Resultado global = resultados.stream()
                .filter(Resultado::isEsGlobal)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el resultado global"));

        List<ResultadoResponse.ResultadoDominioItem> porDominio = resultados.stream()
                .filter(r -> !r.isEsGlobal())
                .map(r -> new ResultadoResponse.ResultadoDominioItem(
                        r.getDominio().getId(),
                        r.getDominio().getNombre(),
                        r.getCoberturaPorcentaje(),
                        r.getDominio().getPesoRelativo()
                ))
                .collect(Collectors.toList());

        String nombreNivel = global.getNivelMadurez() != null ? global.getNivelMadurez().getNombre() : "Sin clasificar";

        return new ResultadoResponse(global.getCoberturaPorcentaje(), nombreNivel, porDominio);
    }
}