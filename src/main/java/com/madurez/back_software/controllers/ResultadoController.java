package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.ResultadoResponse;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.ResultadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluaciones/{evaluacionId}/resultado")
public class ResultadoController {

    @Autowired
    private ResultadoService resultadoService;

    @GetMapping
    public ResponseEntity<?> obtenerResultado(@PathVariable Long evaluacionId,
                                              @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(resultadoService.obtenerResultado(evaluacionId, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}