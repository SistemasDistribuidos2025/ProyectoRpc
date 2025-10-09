package com.myorg.kafka_module.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myorg.kafka_module.dto.SolicitudDonacionDTO;
import com.myorg.kafka_module.model.SolicitudDonacion;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SolicitudDonacionService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "solicitud-donaciones";

    public SolicitudDonacionService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarSolicitud(SolicitudDonacionDTO solicitud) {
        kafkaTemplate.send(TOPIC, solicitud);
        System.out.println("📤 Solicitud enviada: " + solicitud);
    }
}