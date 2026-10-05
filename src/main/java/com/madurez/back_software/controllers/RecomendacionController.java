package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.CrearRecomendacionRequest;
import com.madurez.back_software.dtos.RecomendacionResponse;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    @Autowired
    private RecomendacionService recomendacionService;

    @PostMapping
    public ResponseEntity<?> crearRecomendacion(@RequestBody CrearRecomendacionRequest request,
                                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(recomendacionService.crearRecomendacion(request, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pregunta/{preguntaId}")
    public ResponseEntity<List<RecomendacionResponse>> listarPorPregunta(@PathVariable Long preguntaId) {
        return ResponseEntity.ok(recomendacionService.listarPorPregunta(preguntaId));
    }
    @GetMapping("/evaluacion/{evaluacionId}/sugeridas")
    public ResponseEntity<?> obtenerSugeridas(@PathVariable Long evaluacionId,
                                              @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(
                    recomendacionService.obtenerSugeridasPorEvaluacion(evaluacionId, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/evaluacion/{evaluacionId}/priorizadas")
    public ResponseEntity<?> obtenerPriorizadas(@PathVariable Long evaluacionId,
                                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(
                    recomendacionService.obtenerPriorizadasPorEvaluacion(evaluacionId, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
