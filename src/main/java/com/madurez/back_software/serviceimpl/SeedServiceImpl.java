package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.SeedPreguntaItem;
import com.madurez.back_software.dtos.SeedRequest;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.*;
import com.madurez.back_software.services.DominioService;
import com.madurez.back_software.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SeedServiceImpl implements SeedService {

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RecomendacionRepository recomendacionRepository;

    @Autowired
    private InstrumentoVersionRepository instrumentoVersionRepository;

    @Autowired
    private DominioService dominioService;

    private InstrumentoVersion obtenerVersionVigente() {
        return instrumentoVersionRepository.findByVigenteTrue()
                .orElseGet(() -> {
                    InstrumentoVersion nueva = new InstrumentoVersion();
                    nueva.setNumeroVersion(1);
                    nueva.setVigente(true);
                    return instrumentoVersionRepository.save(nueva);
                });
    }

    private Dominio obtenerOCrearDominio(String nombre, InstrumentoVersion version) {
        return dominioRepository.findByNombre(nombre).orElseGet(() -> {
            Dominio d = new Dominio();
            d.setNombre(nombre);
            d.setDescripcion(nombre);
            d.setPesoRelativo(0.0);
            d.setInstrumentoVersion(version);
            return dominioRepository.save(d);
        });
    }

    private Control obtenerOCrearControl(Norma norma, String anexoA, Dominio dominio, InstrumentoVersion version) {
        return controlRepository.findByNormaAndAnexoAAndDominioId(norma, anexoA, dominio.getId())
                .orElseGet(() -> {
                    Control c = new Control();
                    c.setNorma(norma);
                    c.setAnexoA(anexoA);
                    c.setDominio(dominio);
                    c.setInstrumentoVersion(version);
                    return controlRepository.save(c);
                });
    }

    @Override
    public String ejecutarSeed(SeedRequest request, Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede ejecutar la carga masiva");
        }

        InstrumentoVersion version = obtenerVersionVigente();

        int preguntasCreadas = 0;
        int controlesCreados = 0;
        int recomendacionesCreadas = 0;

        for (SeedPreguntaItem item : request.getPreguntas()) {

            Dominio dominio = obtenerOCrearDominio(item.getDominio().trim(), version);

            Set<Control> controles = new HashSet<>();

            if (item.getControlesISO27001() != null) {
                for (String anexo : item.getControlesISO27001()) {
                    if (anexo == null || anexo.isBlank()) continue; // filtra el '' de la pregunta 20
                    boolean existiaAntes = controlRepository
                            .findByNormaAndAnexoAAndDominioId(Norma.ISO_27001, anexo, dominio.getId()).isPresent();
                    Control c = obtenerOCrearControl(Norma.ISO_27001, anexo, dominio, version);
                    controles.add(c);
                    if (!existiaAntes) controlesCreados++;
                }
            }

            if (item.getControlesISO42001() != null) {
                for (String anexo : item.getControlesISO42001()) {
                    if (anexo == null || anexo.isBlank()) continue;
                    boolean existiaAntes = controlRepository
                            .findByNormaAndAnexoAAndDominioId(Norma.ISO_42001, anexo, dominio.getId()).isPresent();
                    Control c = obtenerOCrearControl(Norma.ISO_42001, anexo, dominio, version);
                    controles.add(c);
                    if (!existiaAntes) controlesCreados++;
                }
            }

            if (controles.isEmpty()) {
                throw new IllegalArgumentException(
                        "La pregunta '" + item.getPregunta() + "' no tiene ningún control válido");
            }

            Pregunta pregunta = new Pregunta();
            pregunta.setTexto(item.getPregunta());
            pregunta.setDominio(dominio);
            pregunta.setInstrumentoVersion(version);
            pregunta.setControles(controles);
            pregunta = preguntaRepository.save(pregunta);
            preguntasCreadas++;

            if (item.getRecomendaciones() != null) {
                for (var entrada : item.getRecomendaciones().entrySet()) {
                    NivelImplementacion nivel = switch (entrada.getKey()) {
                        case "INEXISTENTE" -> NivelImplementacion.NO_EXISTE;
                        case "PARCIAL" -> NivelImplementacion.EXISTE_PARCIALMENTE;
                        default -> throw new IllegalArgumentException("Clave de recomendación desconocida: " + entrada.getKey());
                    };

                    Recomendacion rec = new Recomendacion();
                    rec.setPregunta(pregunta);
                    rec.setNivel(nivel);
                    rec.setDescripcion(entrada.getValue());
                    recomendacionRepository.save(rec);
                    recomendacionesCreadas++;
                }
            }
        }

        // Recalcular pesos de todos los dominios al final, ya con todos los controles creados
        dominioService.recalcularPesos(version.getId());

        return String.format(
                "Seed completado: %d preguntas creadas, %d controles nuevos, %d recomendaciones creadas",
                preguntasCreadas, controlesCreados, recomendacionesCreadas
        );
    }
}