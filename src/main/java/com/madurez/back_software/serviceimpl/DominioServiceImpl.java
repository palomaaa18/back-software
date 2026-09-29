package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.dtos.CrearDominioRequest;
import com.madurez.back_software.dtos.DominioResponse;
import com.madurez.back_software.entities.Dominio;
import com.madurez.back_software.entities.InstrumentoVersion;
import com.madurez.back_software.entities.Rol;
import com.madurez.back_software.entities.Usuario;
import com.madurez.back_software.repositories.ControlRepository;
import com.madurez.back_software.repositories.DominioRepository;
import com.madurez.back_software.repositories.InstrumentoVersionRepository;
import com.madurez.back_software.services.AuditoriaTecnicaService;
import com.madurez.back_software.services.DominioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DominioServiceImpl implements DominioService {

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private InstrumentoVersionRepository instrumentoVersionRepository;

    private void validarAdmin(Usuario ejecutor) {
        if (ejecutor.getRol() != Rol.ADMINISTRADOR) {
            throw new IllegalArgumentException("Solo el Administrador puede configurar dominios");
        }
    }

    // Obtiene la versión vigente del instrumento, o crea la versión 1 si es la primera vez
    private InstrumentoVersion obtenerVersionVigente() {
        return instrumentoVersionRepository.findByVigenteTrue()
                .orElseGet(() -> {
                    InstrumentoVersion nueva = new InstrumentoVersion();
                    nueva.setNumeroVersion(1);
                    nueva.setVigente(true);
                    return instrumentoVersionRepository.save(nueva);
                });
    }

    @Override
    public DominioResponse crearDominio(CrearDominioRequest request, Usuario ejecutor) {
        validarAdmin(ejecutor);

        InstrumentoVersion version = obtenerVersionVigente();

        Dominio dominio = new Dominio();
        dominio.setNombre(request.getNombre());
        dominio.setDescripcion(request.getDescripcion());
        dominio.setPesoRelativo(0.0); // se recalcula cuando se agreguen controles (HU0010)
        dominio.setInstrumentoVersion(version);

        Dominio guardado = dominioRepository.save(dominio);
        auditoriaTecnicaService.registrar(ejecutor, "Dominio", "CREAR",
                "Dominio creado: " + guardado.getNombre());
        return new DominioResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getDescripcion(),
                guardado.getPesoRelativo()
        );
    }

    @Override
    public List<DominioResponse> listarDominios() {
        InstrumentoVersion version = obtenerVersionVigente();

        return dominioRepository.findByInstrumentoVersionId(version.getId()).stream()
                .map(d -> new DominioResponse(d.getId(), d.getNombre(), d.getDescripcion(), d.getPesoRelativo()))
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarDominio(Long dominioId, Usuario ejecutor) {
        validarAdmin(ejecutor);

        Dominio dominio = dominioRepository.findById(dominioId)
                .orElseThrow(() -> new IllegalArgumentException("Dominio no encontrado"));

        if (controlRepository.countByDominioId(dominioId) > 0) {
            throw new IllegalArgumentException(
                    "No se puede eliminar el dominio: tiene controles asociados. Reasigne o elimine los controles primero.");
        }
        auditoriaTecnicaService.registrar(ejecutor, "Dominio", "ELIMINAR",
                "Dominio eliminado: " + dominio.getNombre());
        dominioRepository.delete(dominio);
    }
    @Autowired
    private ControlRepository controlRepository;
    @Override
    public void recalcularPesos(Long instrumentoVersionId) {
        List<Dominio> dominios = dominioRepository.findByInstrumentoVersionId(instrumentoVersionId);
        long totalControles = controlRepository.countByInstrumentoVersionId(instrumentoVersionId);

        if (totalControles == 0) {
            dominios.forEach(d -> d.setPesoRelativo(0.0));
        } else {
            for (Dominio d : dominios) {
                long controlesDominio = controlRepository.countByDominioId(d.getId());
                double peso = (controlesDominio * 100.0) / totalControles;
                d.setPesoRelativo(peso);
            }
        }

        dominioRepository.saveAll(dominios);
    }
    @Autowired
    private AuditoriaTecnicaService auditoriaTecnicaService;
}