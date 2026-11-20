package com.madurez.back_software.serviceimpl;

import com.madurez.back_software.entities.NivelMadurez;
import com.madurez.back_software.repositories.NivelMadurezRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Determina el nivel de madurez de una cobertura global.
 *
 * Reglas:
 *  - La cobertura se redondea a 2 decimales antes de clasificar: la suma ponderada de coberturas
 *    arrastra error de coma flotante (p. ej. 59.99999999999999 o 100.00000000000001) que dejaba
 *    la evaluación en el nivel equivocado o "Sin clasificar".
 *  - El valor límite pertenece al nivel cuyo rango empieza en ese valor (60 -> Gestionado, no Definido).
 *  - 100 pertenece al último nivel (Optimizado).
 */
@Component
public class ClasificadorNivelMadurez {

    public static final String SIN_CLASIFICAR = "Sin clasificar";

    @Autowired
    private NivelMadurezRepository nivelMadurezRepository;

    /** Redondea a 2 decimales y limita el valor al rango 0-100. */
    public static double normalizar(double cobertura) {
        if (Double.isNaN(cobertura) || Double.isInfinite(cobertura)) return 0.0;
        double redondeada = BigDecimal.valueOf(cobertura).setScale(2, RoundingMode.HALF_UP).doubleValue();
        return Math.min(100.0, Math.max(0.0, redondeada));
    }

    /** Nivel que corresponde a la cobertura, o null si no hay niveles configurados. */
    public NivelMadurez clasificar(Double cobertura) {
        if (cobertura == null) return null;

        List<NivelMadurez> niveles = nivelMadurezRepository.findAllByOrderByRangoMinAsc();
        if (niveles.isEmpty()) return null;

        double valor = normalizar(cobertura);
        NivelMadurez elegido = niveles.get(0);

        // El último nivel cuyo rango empieza en o antes del valor
        for (NivelMadurez n : niveles) {
            if (n.getRangoMin() <= valor) {
                elegido = n;
            } else {
                break;
            }
        }
        return elegido;
    }

    public String nombre(Double cobertura) {
        NivelMadurez n = clasificar(cobertura);
        return n != null ? n.getNombre() : SIN_CLASIFICAR;
    }
}
