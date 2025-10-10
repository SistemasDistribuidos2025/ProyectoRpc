package com.myorg.kafka_module.controller;

import com.myorg.kafka_module.dto.EventoDTO;
import com.myorg.kafka_module.service.EventoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @PostMapping("/publicar")
    public void publicarEvento(@RequestBody EventoDTO evento) {
        service.publicarEvento(evento);
    }

    @GetMapping("/externos")
    public List<EventoDTO> obtenerExternos() {
        return service.obtenerEventosExternos();
    }
}
