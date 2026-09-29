package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.AuditoriaTecnicaResponse;
import com.madurez.back_software.entities.Rol;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaTecnicaController {

    @Autowired
    private AuditoriaTecnicaService auditoriaTecnicaService;

    @GetMapping
    public ResponseEntity<?> listarTodo(@AuthenticationPrincipal UsuarioPrincipal principal) {
        if (principal.getUsuario().getRol() != Rol.ADMINISTRADOR) {
            return ResponseEntity.status(403).body("Solo el Administrador puede ver la auditoría técnica");
        }
        return ResponseEntity.ok(auditoriaTecnicaService.listarTodo());
    }

    @GetMapping("/entidad/{entidad}")
    public ResponseEntity<?> listarPorEntidad(@PathVariable String entidad,
                                              @AuthenticationPrincipal UsuarioPrincipal principal) {
        if (principal.getUsuario().getRol() != Rol.ADMINISTRADOR) {
            return ResponseEntity.status(403).body("Solo el Administrador puede ver la auditoría técnica");
        }
        return ResponseEntity.ok(auditoriaTecnicaService.listarPorEntidad(entidad));
    }
}