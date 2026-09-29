package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    List<Pregunta> findByDominioId(Long dominioId);

    List<Pregunta> findByInstrumentoVersionId(Long instrumentoVersionId);
}