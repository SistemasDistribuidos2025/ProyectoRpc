package com.myorg.kafka_module.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myorg.kafka_module.consumer.AdhesionConsumer;
import com.myorg.kafka_module.dto.AdhesionDTO;
import com.myorg.kafka_module.producer.AdhesionProducer;

@Service
public class AdhesionService {
    private final AdhesionProducer producer;
    private final AdhesionConsumer consumer;

    public AdhesionService(AdhesionProducer producer, AdhesionConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public void notificarAdhesion(AdhesionDTO adhesion) {
        if (adhesion.getIdOrganizador() == null || adhesion.getIdOrganizador().isEmpty()) {
            adhesion.setIdOrganizador("ONG001"); 
        }
        producer.enviarAdhesion(adhesion);
    }

    public List<AdhesionDTO> obtenerAdhesionesRecibidas() {
        return consumer.getAdhesionesRecibidas();
    }
}
