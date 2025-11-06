package com.myorg.kafka_module.service;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.consumer.EventoConsumer;
import com.myorg.kafka_module.dto.EventoDTO;

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
        System.out.println("Evento enviado: " + evento);
    }


    public List<EventoDTO> obtenerEventosPropios() {
        return consumer.getEventosPropios();
    }

    public void darDeBajaEvento(String idEvento) {
        EventoDTO evento = consumer.getEventosPropios()
                .stream()
                .filter(e -> e.getIdEvento().equals(idEvento))
                .findFirst()
                .orElse(null);

        if (evento != null) {
            kafkaTemplate.send(TOPIC_BAJA_EVENTO, evento);
            System.out.println("Evento dado de baja enviado a Kafka: " + evento);
            System.out.println("Evento dado de baja enviado a Kafka: " + evento.getNombreEvento());
        } else {
            System.out.println("No se encontró el evento propio con ID " + idEvento);
        }
    }

    public List<EventoDTO> obtenerEventosExternos() {
        return consumer.getEventosExternos();
    }
}
