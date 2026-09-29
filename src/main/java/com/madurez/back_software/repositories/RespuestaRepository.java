package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    List<Respuesta> findByEvaluacionId(Long evaluacionId);

    Optional<Respuesta> findByEvaluacionIdAndPreguntaId(Long evaluacionId, Long preguntaId);

    long countByEvaluacionId(Long evaluacionId);
}