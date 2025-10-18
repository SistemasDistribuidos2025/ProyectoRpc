package com.myorg.kafka_module.producer;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.myorg.kafka_module.dto.ItemDonacionDTO;
import com.myorg.kafka_module.dto.SolicitudDonacionDTO;

@Component
public class SolicitudesExtProducer {

    private static final String TOPIC = "solicitud-donaciones";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final Random random = new Random();
    private final List<String> categorias = List.of("ALIMENTOS", "ROPA", "UTILES_ESCOLARES", "JUGUETES");
    private final List<String> descripciones = List.of("Arroz", "Camperas", "Cuadernos", "Muñecos");

    @Scheduled(fixedRate = 60000) // cada 60 segundos genera una nueva solicitud externa

    public void generarSolicitud() {
        String idOrg = "ORG-" + (random.nextInt(5) + 1);
        String idSolicitud = "SOL-" + System.currentTimeMillis();

        ItemDonacionDTO item = new ItemDonacionDTO(
                categorias.get(random.nextInt(categorias.size())),
                descripciones.get(random.nextInt(descripciones.size())),
                random.nextInt(10) + 1
        );

        SolicitudDonacionDTO dto = new SolicitudDonacionDTO();
        dto.setIdOrganizacion(idOrg);
        dto.setIdSolicitud(idSolicitud);
        dto.setDonaciones(List.of(item));

        kafkaTemplate.send(TOPIC, dto);
        System.out.println("[Automatico] solicitud enviada: " + dto);
    }
    
}
