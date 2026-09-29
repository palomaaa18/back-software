package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.ControlResponse;
import com.madurez.back_software.dtos.CrearPreguntaRequest;
import com.madurez.back_software.dtos.PreguntaResponse;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.ControlRepository;
import com.madurez.back_software.repositories.DominioRepository;
import com.madurez.back_software.repositories.PreguntaRepository;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import com.madurez.back_software.services.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PreguntaServiceImpl implements PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Override
    public PreguntaResponse crearPregunta(CrearPreguntaRequest request, Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede configurar preguntas");
        }

        // HU0009 esc.2: no se permite guardar sin al menos un control vinculado
        if (request.getControlIds() == null || request.getControlIds().isEmpty()) {
            throw new IllegalArgumentException("Debe vincular la pregunta al menos a un control");
        }

        // El dominio de la pregunta se obtiene automáticamente del/los control(es),
        // así que dominioId del request es opcional/informativo — usamos el del primer control.
        Set<Control> controles = new HashSet<>();
        for (Long controlId : request.getControlIds()) {
            Control control = controlRepository.findById(controlId)
                    .orElseThrow(() -> new IllegalArgumentException("Control no encontrado: " + controlId));
            controles.add(control);
        }

        // Validación de consistencia: todos los controles deben pertenecer al mismo dominio,
        // porque la pregunta se asocia automáticamente al dominio de sus controles (HU0009).
        Dominio dominio = controles.iterator().next().getDominio();
        boolean mismoDominio = controles.stream().allMatch(c -> c.getDominio().getId().equals(dominio.getId()));
        if (!mismoDominio) {
            throw new IllegalArgumentException(
                    "Todos los controles vinculados deben pertenecer al mismo dominio");
        }

        Pregunta pregunta = new Pregunta();
        pregunta.setTexto(request.getTexto());
        pregunta.setDominio(dominio);
        pregunta.setInstrumentoVersion(dominio.getInstrumentoVersion());
        pregunta.setControles(controles);

        Pregunta guardada = preguntaRepository.save(pregunta);
        auditoriaTecnicaService.registrar(ejecutor, "Pregunta", "CREAR",
                "Pregunta creada en dominio " + dominio.getNombre() + ": " + guardada.getTexto());
        return mapearAResponse(guardada);
    }

    @Override
    public List<PreguntaResponse> listarPreguntas() {
        return preguntaRepository.findAll().stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PreguntaResponse> listarPorDominio(Long dominioId) {
        return preguntaRepository.findByDominioId(dominioId).stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private PreguntaResponse mapearAResponse(Pregunta p) {
        List<ControlResponse> controlesResponse = p.getControles().stream()
                .map(c -> new ControlResponse(
                        c.getId(), c.getNorma().name(), c.getAnexoA(),
                        c.getDominio().getId(), c.getDominio().getNombre()))
                .collect(Collectors.toList());

        return new PreguntaResponse(
                p.getId(), p.getTexto(), p.getDominio().getId(), p.getDominio().getNombre(), controlesResponse
        );
    }
    @Autowired
    private AuditoriaTecnicaService auditoriaTecnicaService;
}