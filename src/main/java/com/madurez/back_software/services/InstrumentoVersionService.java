package com.madurez.back_software.services;

import com.madurez.back_software.dtos.VersionInstrumentoResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface InstrumentoVersionService {

    List<VersionInstrumentoResponse> listarVersiones(Usuario ejecutor);

    // Copia el catalogo vigente (dominios, controles, preguntas y recomendaciones) a una version nueva
    VersionInstrumentoResponse publicarNuevaVersion(String nota, Usuario ejecutor);
}
