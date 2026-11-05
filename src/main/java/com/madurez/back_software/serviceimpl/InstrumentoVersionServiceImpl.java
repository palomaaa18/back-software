package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.VersionInstrumentoResponse;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.*;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import com.madurez.back_software.services.InstrumentoVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InstrumentoVersionServiceImpl implements InstrumentoVersionService {

    @Autowired
    private InstrumentoVersionRepository instrumentoVersionRepository;

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RecomendacionRepository recomendacionRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private AuditoriaTecnicaService auditoriaTecnicaService;

    private void exigirAdministrador(Usuario ejecutor, String accion) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede " + accion);
        }
    }

    private VersionInstrumentoResponse mapear(InstrumentoVersion v) {
        return new VersionInstrumentoResponse(
                v.getId(), v.getNumeroVersion(), v.getFechaPublicacion(), v.isVigente(), v.getNota(),
                dominioRepository.findByInstrumentoVersionId(v.getId()).size(),
                controlRepository.countByInstrumentoVersionId(v.getId()),
                preguntaRepository.findByInstrumentoVersionId(v.getId()).size()
        );
    }

    @Override
    public List<VersionInstrumentoResponse> listarVersiones(Usuario ejecutor) {
        exigirAdministrador(ejecutor, "consultar las versiones del instrumento");

        return instrumentoVersionRepository.findAllByOrderByNumeroVersionDesc().stream()
                .map(this::mapear)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VersionInstrumentoResponse publicarNuevaVersion(String nota, Usuario ejecutor) {
        exigirAdministrador(ejecutor, "publicar versiones del instrumento");

        if (nota == null || nota.isBlank()) {
            throw new IllegalArgumentException("Debe registrar la nota de la versión");
        }

        // Una evaluación en curso sigue usando las preguntas de su versión: no se cambia el instrumento a mitad
        long enCurso = evaluacionRepository.countByEstado(EstadoEvaluacion.EN_CURSO);
        if (enCurso > 0) {
            throw new IllegalArgumentException(
                    "No se puede publicar una nueva versión: hay " + enCurso +
                            " evaluación(es) en curso. Deben finalizarse primero.");
        }

        InstrumentoVersion vigente = instrumentoVersionRepository.findByVigenteTrue()
                .orElseThrow(() -> new IllegalArgumentException("No hay una versión vigente del instrumento"));

        int siguiente = instrumentoVersionRepository.findAllByOrderByNumeroVersionDesc().stream()
                .map(InstrumentoVersion::getNumeroVersion).findFirst().orElse(0) + 1;

        InstrumentoVersion nueva = new InstrumentoVersion();
        nueva.setNumeroVersion(siguiente);
        nueva.setFechaPublicacion(LocalDateTime.now());
        nueva.setNota(nota.trim());
        nueva.setVigente(true);

        vigente.setVigente(false);
        instrumentoVersionRepository.save(vigente);
        nueva = instrumentoVersionRepository.save(nueva);

        // Dominios
        Map<Long, Dominio> dominios = new HashMap<>();
        for (Dominio d : dominioRepository.findByInstrumentoVersionId(vigente.getId())) {
            Dominio copia = new Dominio();
            copia.setNombre(d.getNombre());
            copia.setDescripcion(d.getDescripcion());
            copia.setPesoRelativo(d.getPesoRelativo());
            copia.setInstrumentoVersion(nueva);
            dominios.put(d.getId(), dominioRepository.save(copia));
        }

        // Controles
        Map<Long, Control> controles = new HashMap<>();
        for (Control c : controlRepository.findByInstrumentoVersionId(vigente.getId())) {
            Control copia = new Control();
            copia.setNorma(c.getNorma());
            copia.setAnexoA(c.getAnexoA());
            copia.setDominio(dominios.get(c.getDominio().getId()));
            copia.setInstrumentoVersion(nueva);
            controles.put(c.getId(), controlRepository.save(copia));
        }

        // Preguntas y sus recomendaciones
        int totalPreguntas = 0;
        for (Pregunta p : preguntaRepository.findByInstrumentoVersionId(vigente.getId())) {
            Pregunta copia = new Pregunta();
            copia.setTexto(p.getTexto());
            copia.setDominio(dominios.get(p.getDominio().getId()));
            copia.setInstrumentoVersion(nueva);

            Set<Control> asociados = new HashSet<>();
            for (Control c : p.getControles()) {
                Control nuevoControl = controles.get(c.getId());
                if (nuevoControl != null) asociados.add(nuevoControl);
            }
            copia.setControles(asociados);
            copia = preguntaRepository.save(copia);
            totalPreguntas++;

            for (Recomendacion r : recomendacionRepository.findByPreguntaId(p.getId())) {
                Recomendacion rc = new Recomendacion();
                rc.setPregunta(copia);
                rc.setNivel(r.getNivel());
                rc.setDescripcion(r.getDescripcion());
                recomendacionRepository.save(rc);
            }
        }

        auditoriaTecnicaService.registrar(ejecutor, "InstrumentoVersion", "PUBLICAR",
                "Versión " + siguiente + " publicada (" + dominios.size() + " dominios, " + controles.size()
                        + " controles, " + totalPreguntas + " preguntas). Nota: " + nueva.getNota());

        return mapear(nueva);
    }
}
