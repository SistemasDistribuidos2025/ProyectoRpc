package com.myorg.kafka_module.producer;

import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaDonacionProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void enviarTransferencia(TransferenciaDonacionDTO transferencia) {
        // El topic depende de la organización receptora (solicitante)
        String topic = "transferencia-donaciones-" + transferencia.getIdOrganizacionReceptora();

        kafkaTemplate.send(topic, transferencia);
        System.out.println("📤 Transferencia enviada al topic: " + topic);
    }
}
