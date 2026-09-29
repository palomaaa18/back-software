package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.SeedRequest;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/seed")
public class SeedController {

    @Autowired
    private SeedService seedService;

    @PostMapping("/preguntas")
    public ResponseEntity<?> ejecutarSeed(@RequestBody SeedRequest request,
                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            String resultado = seedService.ejecutarSeed(request, principal.getUsuario());
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}