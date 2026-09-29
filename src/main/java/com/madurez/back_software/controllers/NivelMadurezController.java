package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.ConfigurarNivelesMadurezRequest;
import com.madurez.back_software.dtos.NivelMadurezResponse;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.NivelMadurezService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/niveles-madurez")
public class NivelMadurezController {

    @Autowired
    private NivelMadurezService nivelMadurezService;

    @PostMapping
    public ResponseEntity<?> configurarNiveles(@RequestBody ConfigurarNivelesMadurezRequest request,
                                               @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            Usuario ejecutor = principal.getUsuario();
            return ResponseEntity.ok(nivelMadurezService.configurarNiveles(request, ejecutor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<NivelMadurezResponse>> listarNiveles() {
        return ResponseEntity.ok(nivelMadurezService.listarNiveles());
    }
}