package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.NivelImplementacion;
import com.madurez.back_software.entities.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {

    List<Recomendacion> findByPreguntaId(Long preguntaId);

    Optional<Recomendacion> findByPreguntaIdAndNivel(Long preguntaId, NivelImplementacion nivel);
}