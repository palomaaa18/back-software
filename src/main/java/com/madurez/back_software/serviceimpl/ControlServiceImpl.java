package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.ControlResponse;
import com.madurez.back_software.dtos.CrearControlRequest;
import com.madurez.back_software.entities.*;
import com.madurez.back_software.repositories.ControlRepository;
import com.madurez.back_software.repositories.DominioRepository;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import com.madurez.back_software.services.ControlService;
import com.madurez.back_software.services.DominioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ControlServiceImpl implements ControlService {

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private DominioService dominioService;

    @Override
    public ControlResponse crearControl(CrearControlRequest request, Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede registrar controles");
        }

        if (request.getDominioId() == null) {
            throw new IllegalArgumentException("El campo dominio es obligatorio");
        }

        Dominio dominio = dominioRepository.findById(request.getDominioId())
                .orElseThrow(() -> new IllegalArgumentException("Dominio no encontrado"));

        Norma norma;
        try {
            norma = Norma.valueOf(request.getNorma());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Norma inválida: " + request.getNorma());
        }

        Control control = new Control();
        control.setNorma(norma);
        control.setAnexoA(request.getAnexoA());
        control.setDominio(dominio);
        control.setInstrumentoVersion(dominio.getInstrumentoVersion());

        Control guardado = controlRepository.save(control);

        // Recalcular pesos de todos los dominios de esta versión (HU0010)
        dominioService.recalcularPesos(dominio.getInstrumentoVersion().getId());
        auditoriaTecnicaService.registrar(ejecutor, "Control", "CREAR",
                "Control creado: " + guardado.getNorma() + " " + guardado.getAnexoA() + " en dominio " + dominio.getNombre());
        return new ControlResponse(
                guardado.getId(),
                guardado.getNorma().name(),
                guardado.getAnexoA(),
                dominio.getId(),
                dominio.getNombre()
        );
    }

    @Override
    public List<ControlResponse> listarControles() {
        return controlRepository.findAll().stream()
                .map(c -> new ControlResponse(
                        c.getId(), c.getNorma().name(), c.getAnexoA(),
                        c.getDominio().getId(), c.getDominio().getNombre()))
                .collect(Collectors.toList());
    }
    @Autowired
    private AuditoriaTecnicaService auditoriaTecnicaService;
}