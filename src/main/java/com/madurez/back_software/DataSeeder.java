package com.madurez.back_software;

import com.madurez.back_software.entities.EstadoUsuario;
import com.madurez.back_software.entities.Rol;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        boolean existeAdmin = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getRol() == Rol.ADMINISTRADOR);

        if (existeAdmin) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setNombre("Administrador Inicial");
        admin.setEmail("admin@madurez.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRol(Rol.ADMINISTRADOR);
        admin.setEstado(EstadoUsuario.ACTIVO);

        usuarioRepository.save(admin);

        System.out.println("========================================");
        System.out.println("Usuario Administrador creado automáticamente:");
        System.out.println("  email: admin@madurez.com");
        System.out.println("  password: admin123");
        System.out.println("========================================");
    }
}