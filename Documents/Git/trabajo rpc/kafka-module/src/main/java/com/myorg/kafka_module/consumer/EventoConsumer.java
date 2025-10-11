package com.myorg.kafka_module.consumer;


import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.EventoDTO;

@Service
public class EventoConsumer {

    private static final String ID_ORGANIZACION_PROPIA = "ONG001"; // cambiar por la real
    private final List<EventoDTO> eventosExternos = new ArrayList<>();

    @KafkaListener(topics = "eventos-solidarios", groupId = "grupo_organizacion")
    public void recibirEvento(EventoDTO evento) {

        if (!evento.getIdOrganizacion().equals(ID_ORGANIZACION_PROPIA)) {
            eventosExternos.add(evento);
            System.out.println("📥 Evento externo recibido: " + evento);
        } else {
            System.out.println("🔸 Evento propio ignorado");
        }
    }

    @KafkaListener(topics = "baja-evento-solidario", groupId = "grupo-eventos")
    public void procesarBajaEvento(EventoDTO evento) {
        System.out.println("Baja recibida de Kafka, Org: " 
            + evento.getIdOrganizacion() + ", Evento: " + evento.getIdEvento());
    }

    public List<EventoDTO> getEventosExternos() {
        return eventosExternos;
    }
}
