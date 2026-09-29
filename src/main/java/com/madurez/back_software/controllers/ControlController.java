package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.ControlResponse;
import com.madurez.back_software.dtos.CrearControlRequest;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.ControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/controles")
public class ControlController {

    @Autowired
    private ControlService controlService;

    @PostMapping
    public ResponseEntity<?> crearControl(@RequestBody CrearControlRequest request,
                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            Usuario ejecutor = principal.getUsuario();
            ControlResponse response = controlService.crearControl(request, ejecutor);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ControlResponse>> listarControles() {
        return ResponseEntity.ok(controlService.listarControles());
    }
}