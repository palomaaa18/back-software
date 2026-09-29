package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.Organizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizacionRepository extends JpaRepository<Organizacion, Long> {

    Optional<Organizacion> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}