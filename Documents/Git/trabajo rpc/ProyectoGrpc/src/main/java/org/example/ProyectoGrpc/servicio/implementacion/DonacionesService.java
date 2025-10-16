package org.example.ProyectoGrpc.servicio.implementacion;

import com.myorg.kafka_module.consumer.OfertaDonacionConsumer;
import com.myorg.kafka_module.consumer.SolicitudDonacionConsumer;
import com.myorg.kafka_module.consumer.TransferenciaDonacionConsumer;
import com.myorg.kafka_module.dto.BajaSolicitudDTO;
import com.myorg.kafka_module.dto.OfertaDonacionDTO;
import com.myorg.kafka_module.dto.SolicitudDonacionDTO;
import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import com.myorg.kafka_module.producer.BajaSolicitudProducer;
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
    private final BajaSolicitudProducer bajaSolicitudProducer;


    private final SolicitudDonacionConsumer solicitudConsumer;
    private final TransferenciaDonacionConsumer transferenciaConsumer;
    private final OfertaDonacionConsumer ofertaConsumer;

    public DonacionesService(SolicitudDonacionService solicitudService, TransferenciaDonacionProducer transferenciaProducer, OfertaDonacionProducer ofertaProducer, SolicitudDonacionConsumer solicitudConsumer, TransferenciaDonacionConsumer transferenciaConsumer, OfertaDonacionConsumer ofertaConsumer, BajaSolicitudProducer bajaSolicitudProducer) {
        this.solicitudService = solicitudService;
        this.transferenciaProducer = transferenciaProducer;
        this.ofertaProducer = ofertaProducer;
        this.bajaSolicitudProducer = bajaSolicitudProducer;
        this.solicitudConsumer = solicitudConsumer;
        this.transferenciaConsumer = transferenciaConsumer;
        this.ofertaConsumer = ofertaConsumer;
    }

    // Solicitar donaciones
    public void solicitarDonaciones(SolicitudDonacionDTO solicitud) {
        System.out.println("📤 DTO a enviar a Kafka: " + solicitud);
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
    public List<SolicitudDonacionDTO> obtenerSolicitudesExternas() {
        return solicitudConsumer.getSolicitudesRecibidas();
    }
    

 
    public boolean darBajaSolicitud(String idOrganizacion, String idSolicitud) {
    
    solicitudConsumer.getSolicitudesRecibidas().forEach(s ->
        System.out.println(" → org=" + s.getIdOrganizacion() + ", sol=" + s.getIdSolicitud())
    );

    boolean existe = solicitudConsumer.getSolicitudesRecibidas()
        .removeIf(s -> s.getIdSolicitud().equals(idSolicitud));


    if (existe) {
        BajaSolicitudDTO baja = new BajaSolicitudDTO(idOrganizacion, idSolicitud);
        bajaSolicitudProducer.enviarBaja(baja);
        System.out.println("Solicitud dada de baja y enviada a Kafka: " + idSolicitud);
    } else {
        System.out.println("No se encontró la solicitud con esos datos.");
    }

    return existe;
}



}
