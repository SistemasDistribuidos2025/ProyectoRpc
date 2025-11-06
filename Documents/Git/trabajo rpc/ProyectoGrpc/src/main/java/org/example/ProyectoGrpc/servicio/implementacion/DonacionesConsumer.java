package org.example.ProyectoGrpc.servicio.implementacion;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;

@Service
public class DonacionesConsumer {

    // MODULO KAFKA PARA CONECTAR AMBAS PARTES Y QUE SE PUEDA MODIFICAR LA BD
    private final DonacionesService donacionesService;

    public DonacionesConsumer(DonacionesService donacionesService) {
        this.donacionesService = donacionesService;
    }

    @KafkaListener(topics = "actualizacion-inventario", groupId = "inventario-group")
    public void actualizarInventario(TransferenciaDonacionDTO transferencia) {

        try {
            donacionesService.transferirDonaciones(transferencia);
            System.out.println("Inventario actualizado correctamente");

        } catch (Exception e) {
            System.err.println("Error al actualizar inventario: " + e.getMessage());
        }
    }
}
