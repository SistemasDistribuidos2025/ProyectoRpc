package com.myorg.kafka_module.controller;

import com.myorg.kafka_module.dto.AdhesionDTO;
import com.myorg.kafka_module.service.AdhesionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/adhesion")
public class AdhesionController {
    private final AdhesionService service;

    public AdhesionController(AdhesionService service) {
        this.service = service;
    }

    @PostMapping
    public void notificarAdhesion(@RequestBody AdhesionDTO adhesion) {
        service.notificarAdhesion(adhesion);
    }

    @GetMapping("/recibidas")
    public List<AdhesionDTO> obtenerRecibidas() {
        return service.obtenerAdhesionesRecibidas();
    }
}
