package com.madurez.back_software.services;

import com.madurez.back_software.dtos.SeedRequest;
import com.madurez.back_software.entities.Usuario;

public interface SeedService {
    String ejecutarSeed(SeedRequest request, Usuario ejecutor);
}