package org.example.ProyectoGrpc.servicio.implementacion;

import com.myorg.kafka_module.consumer.OfertaDonacionConsumer;
import com.myorg.kafka_module.consumer.SolicitudDonacionConsumer;
import com.myorg.kafka_module.consumer.SolicitudExtConsumer;
import com.myorg.kafka_module.consumer.TransferenciaDonacionConsumer;
import com.myorg.kafka_module.dto.BajaSolicitudDTO;
import com.myorg.kafka_module.dto.OfertaDonacionDTO;
import com.myorg.kafka_module.dto.SolicitudDonacionAutomaticoDTO;
import com.myorg.kafka_module.dto.SolicitudDonacionDTO;
import com.myorg.kafka_module.dto.TransferenciaDonacionDTO;
import com.myorg.kafka_module.producer.BajaSolicitudProducer;
import com.myorg.kafka_module.producer.OfertaDonacionProducer;
import com.myorg.kafka_module.producer.TransferenciaDonacionProducer;
import com.myorg.kafka_module.service.SolicitudDonacionService;

import jakarta.transaction.Transactional;

import org.example.ProyectoGrpc.enums.CategoriaDonacion;
import org.example.ProyectoGrpc.repositorioDao.InventarioDonacionesDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final SolicitudExtConsumer solicitudAutomaticaConsumer;

    private final InventarioDonacionesDao inventarioDao;

    public DonacionesService(SolicitudDonacionService solicitudService, TransferenciaDonacionProducer transferenciaProducer, OfertaDonacionProducer ofertaProducer, SolicitudDonacionConsumer solicitudConsumer, TransferenciaDonacionConsumer transferenciaConsumer, OfertaDonacionConsumer ofertaConsumer, BajaSolicitudProducer bajaSolicitudProducer, InventarioDonacionesDao inventarioDao, SolicitudExtConsumer solicitudExtConsumer ) {
        this.solicitudService = solicitudService;
        this.transferenciaProducer = transferenciaProducer;
        this.ofertaProducer = ofertaProducer;
        this.bajaSolicitudProducer = bajaSolicitudProducer;
        this.solicitudConsumer = solicitudConsumer;
        this.transferenciaConsumer = transferenciaConsumer;
        this.ofertaConsumer = ofertaConsumer;
        this.solicitudAutomaticaConsumer = solicitudExtConsumer;
        this.inventarioDao = inventarioDao;
    }

    // Solicitar donaciones
    public void solicitarDonaciones(SolicitudDonacionDTO solicitud) {
        System.out.println("📤 DTO a enviar a Kafka: " + solicitud);
        solicitudService.enviarSolicitud(solicitud);

    }

    // Transferir donaciones
    @Transactional
    public void transferirDonaciones(TransferenciaDonacionDTO transferencia) {

        transferencia.getDonaciones().forEach(item -> {
            try {
                var categoria = CategoriaDonacion.valueOf(item.getCategoria().toUpperCase());
                var descripcion = item.getDescripcion();
                var cantidadTransferida = item.getCantidad();

                var existente = inventarioDao.buscarPorCategoriaYDescripcion(categoria, descripcion);
                System.out.println("Buscando inventario con categoria=" + categoria + " y descripcion=" + descripcion);


                if (existente != null) {
                    int cantidadActual = existente.getCantidad();
                    int nuevaCantidad = cantidadActual - cantidadTransferida;

                    if (nuevaCantidad < 0) {
                        System.out.println("No hay stock suficiente de '" + descripcion + "', se transfiere toda la cantidad disponible");
                        nuevaCantidad = 0;
                    }

                    existente.setCantidad(nuevaCantidad);
                    existente.setFechaHoraModificacion(LocalDateTime.now());
                    inventarioDao.actualizar(existente);

                    System.out.println("Actualizado inventario: " + descripcion +
                            " (" + cantidadActual + " → " + nuevaCantidad + ")");
                } else {
                    
                    System.out.println("No se encontró el item '" + descripcion +
                            "' en el inventario. No se actualiza cantidad.");
                }
            } catch (Exception e) {
                System.err.println("Error procesando item de transferencia: " + e.getMessage());
            }
        });

        System.out.println("Transferencia procesada correctamente.");
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
    
    public List<SolicitudDonacionAutomaticoDTO> obtenerSolicitudesAutomaticas() {
        return solicitudAutomaticaConsumer.getSolicitudesAutomaticas();
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
