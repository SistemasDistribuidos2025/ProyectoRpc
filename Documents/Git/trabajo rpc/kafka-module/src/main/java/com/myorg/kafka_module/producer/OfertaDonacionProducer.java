package com.myorg.kafka_module.producer;

import com.myorg.kafka_module.dto.OfertaDonacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OfertaDonacionProducer {

    private static final String TOPIC = "oferta-donaciones";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void enviarOferta(OfertaDonacionDTO oferta) {
        kafkaTemplate.send(TOPIC, oferta);
        System.out.println("📤 Oferta enviada al topic: " + TOPIC + " → " + oferta.getIdOferta());
    }
}
