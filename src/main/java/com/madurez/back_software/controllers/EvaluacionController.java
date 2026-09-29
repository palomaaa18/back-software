package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.EvaluacionResponse;
import com.madurez.back_software.dtos.IniciarEvaluacionRequest;
import com.madurez.back_software.dtos.ObservarEvaluacionRequest;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<?> iniciarEvaluacion(@RequestBody IniciarEvaluacionRequest request,
                                               @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(evaluacionService.iniciarEvaluacion(request, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mias")
    public ResponseEntity<List<EvaluacionResponse>> listarMisEvaluaciones(@AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.ok(evaluacionService.listarMisEvaluaciones(principal.getUsuario()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEvaluacion(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(evaluacionService.obtenerEvaluacion(id, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarEvaluacion(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            evaluacionService.finalizarEvaluacion(id, principal.getUsuario());
            return ResponseEntity.ok("Evaluación finalizada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}/validar")
    public ResponseEntity<?> validarEvaluacion(@PathVariable Long id,
                                               @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(evaluacionService.validarEvaluacion(id, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/observar")
    public ResponseEntity<?> observarEvaluacion(@PathVariable Long id,
                                                @RequestBody ObservarEvaluacionRequest request,
                                                @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(
                    evaluacionService.observarEvaluacion(id, request.getObservacion(), principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/reevaluar/{organizacionId}")
    public ResponseEntity<?> iniciarReevaluacion(@PathVariable Long organizacionId,
                                                 @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(evaluacionService.iniciarReevaluacion(organizacionId, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/equipo")
    public ResponseEntity<?> listarEvaluacionesEquipo(@AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(evaluacionService.listarEvaluacionesEquipo(principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}