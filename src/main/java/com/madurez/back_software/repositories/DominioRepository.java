package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.Dominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DominioRepository extends JpaRepository<Dominio, Long> {

    List<Dominio> findByInstrumentoVersionId(Long instrumentoVersionId);
    Optional<Dominio> findByNombre(String nombre);
}