package com.madurez.back_software.controllers;

import com.madurez.back_software.dtos.CambiarPasswordRequest;
import com.madurez.back_software.dtos.CrearUsuarioRequest;
import com.madurez.back_software.dtos.UsuarioResponse;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.security.UsuarioPrincipal;
import com.madurez.back_software.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody CrearUsuarioRequest request,
                                          @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            Usuario creador = principal.getUsuario();
            UsuarioResponse response = usuarioService.crearUsuario(request, creador);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long id,
                                               @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            Usuario ejecutor = principal.getUsuario();
            usuarioService.desactivarUsuario(id, ejecutor);
            return ResponseEntity.ok("Usuario desactivado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> listarUsuarios(@AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(usuarioService.listarUsuarios(principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mi-equipo")
    public ResponseEntity<?> listarMiEquipo(@AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            return ResponseEntity.ok(usuarioService.listarMiEquipo(principal.getUsuario()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody CambiarPasswordRequest request,
                                             @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            usuarioService.cambiarPassword(request.getPasswordActual(), request.getPasswordNueva(), principal.getUsuario());
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
