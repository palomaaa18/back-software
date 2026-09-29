package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.EstadoEvaluacion;
import com.madurez.back_software.entities.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByAnalistaIdAndEstado(Long analistaId, EstadoEvaluacion estado);

    List<Evaluacion> findByAnalistaId(Long analistaId);

    List<Evaluacion> findByOrganizacionId(Long organizacionId);

    List<Evaluacion> findByOrganizacionIdAndEstado(Long organizacionId, EstadoEvaluacion estado);
    List<Evaluacion> findByOrganizacionIdAndFechaFinIsNotNullOrderByFechaFinDesc(Long organizacionId);
    List<Evaluacion> findByOrganizacionIdAndFechaFinBetweenOrderByFechaFinDesc(
            Long organizacionId, LocalDateTime desde, LocalDateTime hasta);
    List<Evaluacion> findByJefeId(Long jefeId);
}