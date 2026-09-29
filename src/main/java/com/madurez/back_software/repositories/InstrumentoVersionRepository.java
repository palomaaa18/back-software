package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.InstrumentoVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentoVersionRepository extends JpaRepository<InstrumentoVersion, Long> {

    Optional<InstrumentoVersion> findByVigenteTrue();
}