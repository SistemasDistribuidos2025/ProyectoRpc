package com.myorg.kafka_module.consumer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.AdhesionDTO;

@Service
public class AdhesionConsumer {

    private final List<AdhesionDTO> adhesionesRecibidas = new ArrayList<>();

    @KafkaListener(topics = "adhesion-evento-ONG001", groupId = "grupo_organizacion")
    public void recibirAdhesion(AdhesionDTO adhesion) {
        adhesionesRecibidas.add(adhesion);
        System.out.println("📥 Adhesión recibida: " + adhesion);
    }

    public List<AdhesionDTO> getAdhesionesRecibidas() {
        return adhesionesRecibidas;
    }
}
