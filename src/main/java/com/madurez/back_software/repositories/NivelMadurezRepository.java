package com.madurez.back_software.repositories;

import com.madurez.back_software.entities.NivelMadurez;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NivelMadurezRepository extends JpaRepository<NivelMadurez, Long> {

    List<NivelMadurez> findAllByOrderByRangoMinAsc();

    Optional<NivelMadurez> findByRangoMinLessThanEqualAndRangoMaxGreaterThanEqual(Double valor1, Double valor2);
}