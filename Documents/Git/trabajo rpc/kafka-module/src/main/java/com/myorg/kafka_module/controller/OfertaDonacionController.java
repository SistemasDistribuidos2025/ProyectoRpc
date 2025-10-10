package com.myorg.kafka_module.controller;

import com.myorg.kafka_module.dto.OfertaDonacionDTO;
import com.myorg.kafka_module.consumer.OfertaDonacionConsumer;
import com.myorg.kafka_module.producer.OfertaDonacionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ofertas")
public class OfertaDonacionController {

    @Autowired
    private OfertaDonacionProducer ofertaProducer;

    @Autowired
    private OfertaDonacionConsumer ofertaConsumer;

    @PostMapping
    public String enviarOferta(@RequestBody OfertaDonacionDTO oferta) {
        ofertaProducer.enviarOferta(oferta);
        return "✅ Oferta enviada correctamente a Kafka";
    }

    @GetMapping
    public List<OfertaDonacionDTO> listarOfertas() {
        return ofertaConsumer.getOfertasRecibidas();
    }
}
