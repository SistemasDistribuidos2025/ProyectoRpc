package com.myorg.kafka_module.producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.AdhesionDTO;

@Service
public class AdhesionProducer {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void enviarAdhesion(AdhesionDTO adhesion) {
        String topic = "adhesion-evento/" + adhesion.getIdOrganizador();
        kafkaTemplate.send(topic, adhesion);
        System.out.println("✅ Adhesión enviada a " + topic + ": " +adhesion.getIdEvento() );
    }
}
