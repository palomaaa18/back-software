package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.CrearUsuarioRequest;
import com.madurez.back_software.dtos.UsuarioResponse;
import com.madurez.back_software.entities.EstadoUsuario;
import com.madurez.back_software.entities.Rol;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.repositories.UsuarioRepository;
import com.madurez.back_software.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.madurez.back_software.entities.EstadoEvaluacion;
import com.madurez.back_software.repositories.EvaluacionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponse crearUsuario(CrearUsuarioRequest request, Usuario creador) {

        // Validar email duplicado (HU0001 esc.2, HU0004 esc.2)
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado en el sistema");
        }

        Rol rolSolicitado;
        try {
            rolSolicitado = Rol.valueOf(request.getRol());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + request.getRol());
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setEstado(EstadoUsuario.ACTIVO);

        // Reglas de quién puede crear a quién
        if (creador.getRol() == Rol.ADMINISTRADOR) {
            if (rolSolicitado == Rol.ANALISTA_CIBERSEGURIDAD) {
                throw new IllegalArgumentException(
                        "El Administrador no registra analistas directamente; eso lo hace el Jefe de ciberseguridad");
            }
            nuevoUsuario.setRol(rolSolicitado);

        } else if (creador.getRol() == Rol.JEFE_CIBERSEGURIDAD) {
            if (rolSolicitado != Rol.ANALISTA_CIBERSEGURIDAD) {
                throw new IllegalArgumentException(
                        "El Jefe de ciberseguridad solo puede registrar Analistas de ciberseguridad");
            }
            nuevoUsuario.setRol(Rol.ANALISTA_CIBERSEGURIDAD);
            nuevoUsuario.setJefe(creador); // queda asociado automáticamente (HU0004)

        } else {
            throw new IllegalArgumentException("Tu rol no tiene permiso para crear usuarios");
        }

        Usuario guardado = usuarioRepository.save(nuevoUsuario);

        return new UsuarioResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getEmail(),
                guardado.getRol().name(),
                guardado.getEstado().name(),
                guardado.getJefe() != null ? guardado.getJefe().getId() : null
        );
    }

    @Override
    public void desactivarUsuario(Long usuarioId, Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede desactivar cuentas");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // HU0003 esc.2: no permitir desactivar si tiene evaluaciones en curso
        // (aplica tanto si el usuario es Analista como si es Jefe con evaluaciones asignadas)
        long evaluacionesEnCurso = evaluacionRepository
                .findByAnalistaIdAndEstado(usuario.getId(), EstadoEvaluacion.EN_CURSO)
                .size();

        if (evaluacionesEnCurso > 0) {
            throw new IllegalArgumentException(
                    "No se puede desactivar: el usuario tiene " + evaluacionesEnCurso +
                            " evaluación(es) en curso. Debe reasignarlas o finalizarlas primero.");
        }

        usuario.setEstado(EstadoUsuario.INACTIVO);
        usuarioRepository.save(usuario);
    }
    @Override
    public List<UsuarioResponse> listarUsuarios(Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede ver la lista de usuarios");
        }

        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(
                        u.getId(), u.getNombre(), u.getEmail(), u.getRol().name(),
                        u.getEstado().name(), u.getJefe() != null ? u.getJefe().getId() : null
                ))
                .collect(Collectors.toList());
    }
}
