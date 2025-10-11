package com.myorg.kafka_module.service;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.consumer.EventoConsumer;
import com.myorg.kafka_module.dto.EventoDTO;
import com.myorg.kafka_module.producer.EventoProducer;

@Service
public class EventoService {

    private final EventoConsumer consumer;
    private static final String TOPIC = "eventos-solidarios";
    private static final String TOPIC_BAJA_EVENTO = "baja-evento-solidario";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventoService(KafkaTemplate<String, Object> kafkaTemplate, EventoConsumer consumer) {
        this.kafkaTemplate = kafkaTemplate;
        this.consumer = consumer;
    }

    public void publicarEvento(EventoDTO evento) {
        kafkaTemplate.send(TOPIC, evento);
        System.out.println("Evento enviado: " +evento);
    }

    public void enviarBajaEvento(EventoDTO evento) {
        kafkaTemplate.send(TOPIC_BAJA_EVENTO, evento);
        System.out.println("Evento dado de baja enviado a Kafka: " + evento);
    }


    public List<EventoDTO> obtenerEventosExternos() {
        return consumer.getEventosExternos();
    }
}
