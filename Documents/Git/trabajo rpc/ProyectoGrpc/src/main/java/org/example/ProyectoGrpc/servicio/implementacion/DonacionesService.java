package org.example.ProyectoGrpc.servicio.implementacion;

import com.myorg.kafka_module.consumer.OfertaDonacionConsumer;
import com.myorg.kafka_module.consumer.SolicitudDonacionConsumer;
import com.myorg.kafka_module.consumer.TransferenciaDonacionConsumer;
import com.myorg.kafka_module.dto.OfertaDonacionDTO;
import com.myorg.kafka_module.dto.SolicitudDonacionDTO;
import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import com.myorg.kafka_module.producer.OfertaDonacionProducer;
import com.myorg.kafka_module.producer.TransferenciaDonacionProducer;
import com.myorg.kafka_module.service.SolicitudDonacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonacionesService {

    private final SolicitudDonacionService solicitudService;
    private final TransferenciaDonacionProducer transferenciaProducer;
    private final OfertaDonacionProducer ofertaProducer;

    private final SolicitudDonacionConsumer solicitudConsumer;
    private final TransferenciaDonacionConsumer transferenciaConsumer;
    private final OfertaDonacionConsumer ofertaConsumer;

    public DonacionesService(SolicitudDonacionService solicitudService,
                             TransferenciaDonacionProducer transferenciaProducer,
                             OfertaDonacionProducer ofertaProducer,
                             SolicitudDonacionConsumer solicitudConsumer,
                             TransferenciaDonacionConsumer transferenciaConsumer,
                             OfertaDonacionConsumer ofertaConsumer) {
        this.solicitudService = solicitudService;
        this.transferenciaProducer = transferenciaProducer;
        this.ofertaProducer = ofertaProducer;
        this.solicitudConsumer = solicitudConsumer;
        this.transferenciaConsumer = transferenciaConsumer;
        this.ofertaConsumer = ofertaConsumer;
    }

    // Solicitar donaciones
    public void solicitarDonaciones(SolicitudDonacionDTO solicitud) {
        solicitudService.enviarSolicitud(solicitud);
    }

    // Transferir donaciones
    public void transferirDonaciones(TransferenciaDonacionDTO transferencia) {
        transferenciaProducer.enviarTransferencia(transferencia);
    }

    // Ofrecer donaciones
    public void ofrecerDonaciones(OfertaDonacionDTO oferta) {
        ofertaProducer.enviarOferta(oferta);
    }

    // Consultas
    public List<SolicitudDonacionDTO> obtenerSolicitudesRecibidas() {
        return solicitudConsumer.getSolicitudesRecibidas();
    }

    public List<TransferenciaDonacionDTO> obtenerTransferenciasRecibidas() {
        return transferenciaConsumer.getTransferenciasRecibidas();
    }

    public List<OfertaDonacionDTO> obtenerOfertasRecibidas() {
        return ofertaConsumer.getOfertasRecibidas();
    }
}
