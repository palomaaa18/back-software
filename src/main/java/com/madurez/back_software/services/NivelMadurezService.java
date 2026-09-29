package com.madurez.back_software.services;

import com.madurez.back_software.dtos.ConfigurarNivelesMadurezRequest;
import com.madurez.back_software.dtos.NivelMadurezResponse;
import com.madurez.back_software.entities.Usuario;

import java.util.List;

public interface NivelMadurezService {

    List<NivelMadurezResponse> configurarNiveles(ConfigurarNivelesMadurezRequest request, Usuario ejecutor);

    List<NivelMadurezResponse> listarNiveles();
}