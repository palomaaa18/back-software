package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.OrganizacionResponse;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.OrganizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizaciones")
public class OrganizacionController {

    @Autowired
    private OrganizacionService organizacionService;

    @GetMapping
    public ResponseEntity<?> listarOrganizaciones(@AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(organizacionService.listarOrganizaciones(principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mia")
    public ResponseEntity<?> obtenerMiOrganizacion(@AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(organizacionService.obtenerMiOrganizacion(principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}