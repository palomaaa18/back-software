package com.madurez.back_software.services;

import com.madurez.back_software.dtos.ComparativaResponse;
import com.madurez.back_software.dtos.DashboardResponse;
import com.madurez.back_software.dtos.HistoricoItem;
import com.madurez.back_software.entities.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {

    DashboardResponse obtenerDashboard(Long organizacionId, Usuario ejecutor);
    ComparativaResponse obtenerComparativa(Long organizacionId, Usuario ejecutor);

    List<HistoricoItem> obtenerHistorico(Long organizacionId, Usuario ejecutor);
    List<HistoricoItem> obtenerHistoricoFiltrado(Long organizacionId, LocalDateTime desde, LocalDateTime hasta, Usuario ejecutor);
}