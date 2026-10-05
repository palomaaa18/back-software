package com.madurez.back_software.services;

import com.madurez.back_software.dtos.OrganizacionResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface OrganizacionService {

    List<OrganizacionResponse> listarOrganizaciones(Usuario ejecutor);

    OrganizacionResponse obtenerMiOrganizacion(Usuario ejecutor);
}