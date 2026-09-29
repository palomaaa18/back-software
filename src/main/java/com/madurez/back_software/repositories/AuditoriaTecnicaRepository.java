package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.AuditoriaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaTecnicaRepository extends JpaRepository<AuditoriaTecnica, Long> {

    List<AuditoriaTecnica> findAllByOrderByFechaDesc();

    List<AuditoriaTecnica> findByEntidadAfectadaOrderByFechaDesc(String entidadAfectada);
}