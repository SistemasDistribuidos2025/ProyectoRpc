package com.myorg.kafka_module.consumer;

import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferenciaDonacionConsumer {

    private final List<TransferenciaDonacionDTO> transferenciasRecibidas = new ArrayList<>();

    // Escucha transferencias que tengan el id de nuestra organización como parte del topic
    @KafkaListener(topicPattern = "transferencia-donaciones-.*", groupId = "ongs-group")
    public void procesarTransferencia(TransferenciaDonacionDTO transferencia) {
        System.out.println("📥 Transferencia recibida para nuestra organización: "
                + transferencia.getIdOrganizacionReceptora()
                + ", solicitud: " + transferencia.getIdSolicitud());

        transferenciasRecibidas.add(transferencia);
        System.out.println("💾 Transferencias totales almacenadas: " + transferenciasRecibidas.size());

        // 🔧 En el futuro: acá actualizaríamos inventarios (sumar o restar cantidades)
    }

    public List<TransferenciaDonacionDTO> getTransferenciasRecibidas() {
        return transferenciasRecibidas;
    }
}