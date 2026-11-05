package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.PublicarVersionRequest;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.InstrumentoVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instrumento-versiones")
public class InstrumentoVersionController {

    @Autowired
    private InstrumentoVersionService instrumentoVersionService;

    @GetMapping
    public ResponseEntity<?> listar(@AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(instrumentoVersionService.listarVersiones(principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/publicar")
    public ResponseEntity<?> publicar(@RequestBody PublicarVersionRequest request,
                                      @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(
                    instrumentoVersionService.publicarNuevaVersion(request.getNota(), principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
