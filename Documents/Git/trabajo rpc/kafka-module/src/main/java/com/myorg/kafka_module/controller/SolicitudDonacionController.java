package com.myorg.kafka_module.controller;

import com.myorg.kafka_module.dto.SolicitudDonacionDTO;
import com.myorg.kafka_module.service.SolicitudDonacionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudDonacionController {

    private final SolicitudDonacionService service;

    public SolicitudDonacionController(SolicitudDonacionService service) {
        this.service = service;
    }

    @PostMapping
    public String crearSolicitud(@RequestBody SolicitudDonacionDTO solicitud) {
        service.enviarSolicitud(solicitud);
        return "Solicitud enviada correctamente";
    }

    @PostMapping("/baja")
    public String darBajaSolicitud(@RequestBody SolicitudDonacionDTO baja) {
        service.enviarBajaSolicitud(baja);
        return "Baja de solicitud enviada a Kafka: " + baja.getIdSolicitud();
    }
}

