package com.myorg.kafka_module.consumer;

import com.myorg.kafka_module.dto.OfertaDonacionDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfertaDonacionConsumer {

    private final List<OfertaDonacionDTO> ofertasRecibidas = new ArrayList<>();

    @KafkaListener(topics = "oferta-donaciones", groupId = "ongs-group")
    public void procesarOferta(OfertaDonacionDTO oferta) {
        System.out.println("📥 Oferta recibida de organización: " + oferta.getIdOrganizacionDonante()
                + " (Oferta ID: " + oferta.getIdOferta() + ")");

        ofertasRecibidas.add(oferta);
        System.out.println("💾 Ofertas totales almacenadas: " + ofertasRecibidas.size());
    }

    public List<OfertaDonacionDTO> getOfertasRecibidas() {
        return ofertasRecibidas;
    }
}
