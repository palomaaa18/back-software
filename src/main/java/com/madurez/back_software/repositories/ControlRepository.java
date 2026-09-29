package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.Control;
import com.madurez.back_software.entities.Norma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ControlRepository extends JpaRepository<Control, Long> {

    List<Control> findByDominioId(Long dominioId);

    List<Control> findByInstrumentoVersionId(Long instrumentoVersionId);

    long countByDominioId(Long dominioId);

    long countByInstrumentoVersionId(Long instrumentoVersionId);
    Optional<Control> findByNormaAndAnexoAAndDominioId(Norma norma, String anexoA, Long dominioId);
}