package com.myorg.kafka_module.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.BajaSolicitudDTO;

@Service
public class BajaSolicitudProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "baja-solicitud";

    public void enviarBaja(BajaSolicitudDTO baja) {
        kafkaTemplate.send(TOPIC, baja);
        System.out.println("BajaSolicitudDTO enviado a Kafka: " + baja);
    }
    
}
