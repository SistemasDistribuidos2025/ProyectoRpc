package com.myorg.kafka_module.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.EventoDTO;

@Service
public class EventoProducer {
  
    private static final String TOPIC = "eventos-solidarios";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void enviarEvento(EventoDTO evento) {
        kafkaTemplate.send(TOPIC, evento);
        System.out.println("✅ Evento publicado: " +evento.getNombreEvento());
    }
}
