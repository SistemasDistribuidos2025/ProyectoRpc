package com.myorg.kafka_module.consumer;

import com.myorg.kafka_module.dto.SolicitudDonacionDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class SolicitudDonacionConsumer {

    // Lista en memoria para simular almacenamiento
    private final List<SolicitudDonacionDTO> solicitudesRecibidas = new ArrayList<>();

    @KafkaListener(topics = "solicitud-donaciones", groupId = "ongs-group")
    public void procesarSolicitud(SolicitudDonacionDTO solicitud) {
        System.out.println("📥 Solicitud recibida de organización: " + solicitud.getIdOrganizacion()
                + ", idSolicitud: " + solicitud.getIdSolicitud());

        // TODO: cotejar con bajas (punto 4) para descartar solicitudes canceladas

        // Guardar en memoria por ahora
        solicitudesRecibidas.add(solicitud);
        System.out.println("💾 Solicitud agregada a la lista interna. Total: " + solicitudesRecibidas.size());
    }

    // Método para consultar solicitudes recibidas (simula futura integración con front)
    public List<SolicitudDonacionDTO> getSolicitudesRecibidas() {
        return solicitudesRecibidas;
    }
}
