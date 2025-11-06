package com.myorg.kafka_module.service;

import com.myorg.kafka_module.consumer.SolicitudDonacionConsumer;
import com.myorg.kafka_module.dto.SolicitudDonacionDTO;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SolicitudDonacionService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "solicitud-donaciones";
    private static final String TOPIC_BAJA = "baja-solicitud-donaciones";
    private final SolicitudDonacionConsumer consumer;

    public SolicitudDonacionService(KafkaTemplate<String, Object> kafkaTemplate, SolicitudDonacionConsumer consumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.consumer = consumer;
}


    public void enviarSolicitud(SolicitudDonacionDTO solicitud) {
        kafkaTemplate.send(TOPIC, solicitud);
        System.out.println("📤 Solicitud enviada: " + solicitud);
    }

    public void enviarBajaSolicitud(SolicitudDonacionDTO baja) {
        kafkaTemplate.send(TOPIC_BAJA, baja);
        System.out.println("Solicitud enviada (BAJA): " + baja);
    }
    public List<SolicitudDonacionDTO> obtenerSolicitudesExternas() {
        return consumer.getSolicitudesRecibidas();
    }
    
}