package com.myorg.kafka_module.consumer;

import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferenciaDonacionConsumer {

    private final List<TransferenciaDonacionDTO> transferenciasRecibidas = new ArrayList<>();

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TransferenciaDonacionConsumer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topicPattern = "transferencia-donaciones-.*", groupId = "ongs-group")
    public void procesarTransferencia(TransferenciaDonacionDTO transferencia) {

        System.out.println("Transferencia recibida en módulo Kafka: " + transferencia);

        //Se envia la informacion al topic que esta en el consumer que conecta ambas partes
        String topicDestino = "actualizacion-inventario";
        kafkaTemplate.send(topicDestino, transferencia);

    }

    public List<TransferenciaDonacionDTO> getTransferenciasRecibidas() {
        return transferenciasRecibidas;
    }
}