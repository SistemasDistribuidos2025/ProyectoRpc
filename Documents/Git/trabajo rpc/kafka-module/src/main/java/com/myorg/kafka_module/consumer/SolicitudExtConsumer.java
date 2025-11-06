package com.myorg.kafka_module.consumer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.SolicitudDonacionAutomaticoDTO;

@Service
public class SolicitudExtConsumer {

    private final List<SolicitudDonacionAutomaticoDTO> solicitudesAutomaticas = new ArrayList<>();

    @KafkaListener(topics = "solicitud-donaciones-automaticas", groupId = "ongs-group")
    public void procesarSolicitudAutomatica(SolicitudDonacionAutomaticoDTO solicitud) {
        System.out.println("Solicitud automática recibida: " + solicitud.getIdSolicitud() +
                " de organización " + solicitud.getIdOrganizacion());
        solicitudesAutomaticas.add(solicitud);
        System.out.println("Total solicitudes automáticas guardadas: " + solicitudesAutomaticas.size());
    }

    public List<SolicitudDonacionAutomaticoDTO> getSolicitudesAutomaticas() {
        return solicitudesAutomaticas;
    }
}