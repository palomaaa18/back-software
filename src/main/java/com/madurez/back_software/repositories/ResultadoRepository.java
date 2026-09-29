package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    List<Resultado> findByEvaluacionId(Long evaluacionId);

    Optional<Resultado> findByEvaluacionIdAndEsGlobalTrue(Long evaluacionId);

    void deleteByEvaluacionId(Long evaluacionId);
}