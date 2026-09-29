package com.madurez.back_software.services;

import com.madurez.back_software.dtos.CrearDominioRequest;
import com.madurez.back_software.dtos.DominioResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface DominioService {

    DominioResponse crearDominio(CrearDominioRequest request, Usuario ejecutor);

    List<DominioResponse> listarDominios();

    void eliminarDominio(Long dominioId, Usuario ejecutor);
    void recalcularPesos(Long instrumentoVersionId);
}