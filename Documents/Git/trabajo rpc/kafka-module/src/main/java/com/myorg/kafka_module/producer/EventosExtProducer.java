package com.myorg.kafka_module.producer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.myorg.kafka_module.dto.EventoDTO;

@Component
public class EventosExtProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "eventos-solidarios";
    
    public EventosExtProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Cada 60 segundos genera un evento nuevo
    @Scheduled(fixedRate = 60000)
    public void generarEventoExterno() {
        EventoDTO evento = new EventoDTO();

        evento.setIdOrganizacion("ONG" + ((int) (Math.random() * 5) + 2));

        evento.setIdEvento(UUID.randomUUID().toString());
        evento.setNombreEvento("Colecta " + (int) (Math.random() * 100));
        evento.setDescripcion("Evento solidario automatico");
        evento.setFechaHora(
                LocalDateTime.now()
                        .plusMinutes((int) (Math.random() * 120))
                        .truncatedTo(java.time.temporal.ChronoUnit.SECONDS));

        kafkaTemplate.send(TOPIC, evento);
        System.out.println("[Automatico] evento publicado: " + evento);
    }
}
