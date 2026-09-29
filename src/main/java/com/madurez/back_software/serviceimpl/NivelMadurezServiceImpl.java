package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.ConfigurarNivelesMadurezRequest;
import com.madurez.back_software.dtos.NivelMadurezResponse;
import com.madurez.back_software.entities.NivelMadurez;
import com.madurez.back_software.entities.Rol;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.repositories.NivelMadurezRepository;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import com.madurez.back_software.services.NivelMadurezService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NivelMadurezServiceImpl implements NivelMadurezService {

    @Autowired
    private NivelMadurezRepository nivelMadurezRepository;

    @Override
    public List<NivelMadurezResponse> configurarNiveles(ConfigurarNivelesMadurezRequest request, Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede configurar los niveles de madurez");
        }

        if (request.getNiveles() == null || request.getNiveles().size() != 5) {
            throw new IllegalArgumentException("Debe definir exactamente los cinco niveles de madurez");
        }

        // Ordenar por rangoMin para poder validar traslapes en secuencia
        List<ConfigurarNivelesMadurezRequest.NivelItem> ordenados = new ArrayList<>(request.getNiveles());
        ordenados.sort(Comparator.comparing(ConfigurarNivelesMadurezRequest.NivelItem::getRangoMin));

        // Validar que cada rango sea coherente (min <= max)
        for (var item : ordenados) {
            if (item.getRangoMin() == null || item.getRangoMax() == null || item.getRangoMin() > item.getRangoMax()) {
                throw new IllegalArgumentException(
                        "El nivel '" + item.getNombre() + "' tiene un rango inválido");
            }
        }

        // HU0011 esc.2: validar que no haya traslape entre niveles consecutivos.
        // Se consideran consecutivos correctos cuando el máximo de uno coincide
        // exactamente con el mínimo del siguiente (sin solaparse ni dejar huecos).
        for (int i = 0; i < ordenados.size() - 1; i++) {
            var actual = ordenados.get(i);
            var siguiente = ordenados.get(i + 1);
            if (actual.getRangoMax() >= siguiente.getRangoMin() && !actual.getRangoMax().equals(siguiente.getRangoMin())) {
                throw new IllegalArgumentException(
                        "Traslape detectado entre '" + actual.getNombre() + "' y '" + siguiente.getNombre() + "'");
            }
            if (actual.getRangoMax() > siguiente.getRangoMin()) {
                throw new IllegalArgumentException(
                        "Traslape detectado entre '" + actual.getNombre() + "' y '" + siguiente.getNombre() + "'");
            }
        }

        // Validar que cubran de 0 a 100 exactamente (suma continua, sin huecos)
        if (!ordenados.get(0).getRangoMin().equals(0.0)) {
            throw new IllegalArgumentException("El primer nivel debe iniciar en 0");
        }
        if (!ordenados.get(ordenados.size() - 1).getRangoMax().equals(100.0)) {
            throw new IllegalArgumentException("El último nivel debe terminar en 100");
        }

        // Si pasó todas las validaciones, reemplazamos la configuración completa
        nivelMadurezRepository.deleteAll();

        List<NivelMadurez> nuevos = ordenados.stream().map(item -> {
            NivelMadurez n = new NivelMadurez();
            n.setNombre(item.getNombre());
            n.setRangoMin(item.getRangoMin());
            n.setRangoMax(item.getRangoMax());
            return n;
        }).collect(Collectors.toList());

        List<NivelMadurez> guardados = nivelMadurezRepository.saveAll(nuevos);
        auditoriaTecnicaService.registrar(ejecutor, "NivelMadurez", "CONFIGURAR",
                "Configuración completa de los 5 niveles de madurez actualizada");
        return guardados.stream()
                .map(n -> new NivelMadurezResponse(n.getId(), n.getNombre(), n.getRangoMin(), n.getRangoMax()))
                .collect(Collectors.toList());
    }

    @Override
    public List<NivelMadurezResponse> listarNiveles() {
        return nivelMadurezRepository.findAllByOrderByRangoMinAsc().stream()
                .map(n -> new NivelMadurezResponse(n.getId(), n.getNombre(), n.getRangoMin(), n.getRangoMax()))
                .collect(Collectors.toList());
    }
    @Autowired
    private AuditoriaTecnicaService auditoriaTecnicaService;
}