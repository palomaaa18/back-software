package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.GuardarRespuestaRequest;
import com.madurez.back_software.dtos.ProgresoDominioResponse;
import com.madurez.back_software.dtos.RespuestaResponse;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones/{evaluacionId}/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @PostMapping
    public ResponseEntity<?> guardarRespuesta(@PathVariable Long evaluacionId,
                                              @RequestBody GuardarRespuestaRequest request,
                                              @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(respuestaService.guardarRespuesta(evaluacionId, request, principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<RespuestaResponse>> listarRespuestas(@PathVariable Long evaluacionId,
                                                                    @AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.ok(respuestaService.listarRespuestas(evaluacionId, principal.getUsuario()));
    }

    @GetMapping("/progreso")
    public ResponseEntity<List<ProgresoDominioResponse>> obtenerProgreso(@PathVariable Long evaluacionId,
                                                                         @AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.ok(respuestaService.obtenerProgreso(evaluacionId, principal.getUsuario()));
    }
}