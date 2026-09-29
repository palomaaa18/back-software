package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.CrearPreguntaRequest;
import com.madurez.back_software.dtos.PreguntaResponse;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preguntas")
public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;

    @PostMapping
    public ResponseEntity<?> crearPregunta(@RequestBody CrearPreguntaRequest request,
                                           @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            Usuario ejecutor = principal.getUsuario();
            PreguntaResponse response = preguntaService.crearPregunta(request, ejecutor);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PreguntaResponse>> listarPreguntas() {
        return ResponseEntity.ok(preguntaService.listarPreguntas());
    }

    @GetMapping("/dominio/{dominioId}")
    public ResponseEntity<List<PreguntaResponse>> listarPorDominio(@PathVariable Long dominioId) {
        return ResponseEntity.ok(preguntaService.listarPorDominio(dominioId));
    }
}