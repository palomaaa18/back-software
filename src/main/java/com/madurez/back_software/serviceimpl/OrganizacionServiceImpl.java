package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.OrganizacionResponse;
import com.madurez.back_software.entities.Organizacion;
import com.madurez.back_software.entities.Rol;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.repositories.OrganizacionRepository;
import com.madurez.back_software.services.OrganizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizacionServiceImpl implements OrganizacionService {

    @Autowired
    private OrganizacionRepository organizacionRepository;

    @Override
    public List<OrganizacionResponse> listarOrganizaciones(Usuario ejecutor) {
        if (ejecutor.getRol() == Rol.ANALISTA_CIBERSEGURIDAD) {
            throw new IllegalArgumentException("Tu rol no tiene acceso al listado de organizaciones");
        }

        return organizacionRepository.findAll().stream()
                .map(o -> new OrganizacionResponse(o.getId(), o.getNombre(), o.getSector()))
                .collect(Collectors.toList());
    }

    @Override
    public OrganizacionResponse obtenerMiOrganizacion(Usuario ejecutor) {
        if (ejecutor.getOrganizacion() == null) {
            throw new IllegalArgumentException("No tienes una organización asignada");
        }

        Organizacion o = ejecutor.getOrganizacion();
        return new OrganizacionResponse(o.getId(), o.getNombre(), o.getSector());
    }
}