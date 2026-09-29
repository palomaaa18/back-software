package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.AuditoriaTecnicaResponse;
import com.madurez.back_software.entities.AuditoriaTecnica;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.repositories.AuditoriaTecnicaRepository;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditoriaTecnicaServiceImpl implements AuditoriaTecnicaService {

    @Autowired
    private AuditoriaTecnicaRepository auditoriaTecnicaRepository;

    @Override
    public void registrar(Usuario usuario, String entidadAfectada, String accion, String detalle) {
        AuditoriaTecnica auditoria = new AuditoriaTecnica();
        auditoria.setUsuario(usuario);
        auditoria.setEntidadAfectada(entidadAfectada);
        auditoria.setAccion(accion);
        auditoria.setDetalle(detalle);
        auditoriaTecnicaRepository.save(auditoria);
    }

    @Override
    public List<AuditoriaTecnicaResponse> listarTodo() {
        return auditoriaTecnicaRepository.findAllByOrderByFechaDesc().stream()
                .map(this::mapear)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditoriaTecnicaResponse> listarPorEntidad(String entidadAfectada) {
        return auditoriaTecnicaRepository.findByEntidadAfectadaOrderByFechaDesc(entidadAfectada).stream()
                .map(this::mapear)
                .collect(Collectors.toList());
    }

    private AuditoriaTecnicaResponse mapear(AuditoriaTecnica a) {
        return new AuditoriaTecnicaResponse(
                a.getId(), a.getUsuario().getNombre(), a.getEntidadAfectada(),
                a.getAccion(), a.getDetalle(), a.getFecha()
        );
    }
}