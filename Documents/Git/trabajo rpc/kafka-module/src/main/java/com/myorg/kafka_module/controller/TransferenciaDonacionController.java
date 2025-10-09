package com.myorg.kafka_module.controller;

import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import com.myorg.kafka_module.producer.TransferenciaDonacionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/transferencias")
    public class TransferenciaDonacionController {

        @Autowired
        private TransferenciaDonacionProducer transferenciaProducer;

        @PostMapping
        public String enviarTransferencia(@RequestBody TransferenciaDonacionDTO transferencia) {
            transferenciaProducer.enviarTransferencia(transferencia);
            return "✅ Transferencia enviada correctamente a Kafka";
        }
    }