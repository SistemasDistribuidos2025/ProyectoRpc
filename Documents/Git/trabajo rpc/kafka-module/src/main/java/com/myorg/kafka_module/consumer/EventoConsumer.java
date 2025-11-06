package com.myorg.kafka_module.consumer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.EventoDTO;

@Service
public class EventoConsumer {

    private static final String ID_ORGANIZACION_PROPIA = "ONG001";
    private final List<EventoDTO> eventosExternos = new ArrayList<>();
    private final List<EventoDTO> eventosPropios = new ArrayList<>();

    @KafkaListener(topics = "eventos-solidarios", groupId = "grupo_organizacion")
    public void recibirEvento(EventoDTO evento) {
        LocalDateTime ahora = LocalDateTime.now();

        if (evento.getIdOrganizacion().equals(ID_ORGANIZACION_PROPIA)) {
            
            //para la lista de eventos propios y poder darles de baja
            eventosPropios.removeIf(e -> e.getIdEvento().equals(evento.getIdEvento()));
            eventosPropios.add(evento);
            System.out.println("Evento propio guardado: " + evento.getNombreEvento());

        } else if (evento.getFechaHora() != null && evento.getFechaHora().isAfter(ahora)) {
            
            //lista aparte de eventos externos
            eventosExternos.removeIf(e -> e.getIdEvento().equals(evento.getIdEvento()));
            eventosExternos.add(evento);
            System.out.println("Evento externo recibido: " + evento.getNombreEvento());

        } else {
            System.out.println("Evento descartado (propio vencido o sin fecha)");
        }
    }

    @KafkaListener(topics = "baja-evento-solidario", groupId = "grupo-eventos")
    public void procesarBajaEvento(EventoDTO evento) {

            // bajas propias
        if (evento.getIdOrganizacion().equals(ID_ORGANIZACION_PROPIA)) {
            eventosPropios.removeIf(e -> e.getIdEvento().equals(evento.getIdEvento()));
            System.out.println("Evento propio dado de baja: " + evento.getIdEvento());

        } else {
            // bajas de eventos externos
            eventosExternos.removeIf(e -> e.getIdEvento().equals(evento.getIdEvento()));
            System.out.println("🗑️ Evento externo eliminado: " + evento.getIdEvento());
        }
    }

    public List<EventoDTO> getEventosExternos() {
        return eventosExternos;
    }

    public List<EventoDTO> getEventosPropios() {
        return eventosPropios;
    }
}
