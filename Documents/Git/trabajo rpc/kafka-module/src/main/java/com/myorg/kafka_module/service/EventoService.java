package com.myorg.kafka_module.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myorg.kafka_module.consumer.EventoConsumer;
import com.myorg.kafka_module.dto.EventoDTO;
import com.myorg.kafka_module.producer.EventoProducer;

@Service
public class EventoService {
    private final EventoProducer producer;
    private final EventoConsumer consumer;

    public EventoService(EventoProducer producer, EventoConsumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public void publicarEvento(EventoDTO evento) {
        producer.enviarEvento(evento);
    }

    public List<EventoDTO> obtenerEventosExternos() {
        return consumer.getEventosExternos();
    }
}
