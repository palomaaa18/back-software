package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.DashboardResponse;
import com.madurez.back_software.dtos.EvaluacionResponse;
import com.madurez.back_software.dtos.HistoricoItem;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/organizacion/{organizacionId}")
    public ResponseEntity<?> obtenerDashboard(@PathVariable Long organizacionId,
                                              @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(dashboardService.obtenerDashboard(organizacionId, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/organizacion/{organizacionId}/comparativa")
    public ResponseEntity<?> obtenerComparativa(@PathVariable Long organizacionId,
                                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(dashboardService.obtenerComparativa(organizacionId, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/organizacion/{organizacionId}/historico")
    public ResponseEntity<?> obtenerHistorico(@PathVariable Long organizacionId,
                                              @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(dashboardService.obtenerHistorico(organizacionId, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/organizacion/{organizacionId}/historico/filtrado")
    public ResponseEntity<?> obtenerHistoricoFiltrado(
            @PathVariable Long organizacionId,
            @RequestParam("desde") String desde,
            @RequestParam("hasta") String hasta,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            LocalDateTime fechaDesde = LocalDate.parse(desde).atStartOfDay();
            LocalDateTime fechaHasta = LocalDate.parse(hasta).atTime(23, 59, 59);

            List<HistoricoItem> resultado = dashboardService.obtenerHistoricoFiltrado(
                    organizacionId, fechaDesde, fechaHasta, principal.getUsuario());

            if (resultado.isEmpty()) {
                return ResponseEntity.ok().body(
                        java.util.Map.of("mensaje", "No se encontraron resultados para el filtro aplicado", "datos", resultado));
            }

            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (java.time.format.DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido, use YYYY-MM-DD");
        }
    }
}