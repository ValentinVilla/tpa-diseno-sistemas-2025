package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.fuentes.ClienteMetaMapa;
import ar.edu.utn.frba.dds.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.servicios.SolicitudService;
import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SolicitarEliminacionTest {


  @Test
  public void solicitudEsRechazadaAutomaticamenteSiEsSpam() {
    Hecho hecho = new HechoBuilder()
        .titulo("Spam Title")
        .descripcion("Spam description")
        .categoria("fake-news")
        .latitud(0.0)
        .longitud(0.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .origen(Origen.CARGAMANUAL)
        .visible(true)
        .build();

    DetectorDeSpam detectorSiempreTrue = texto -> true;
    RepositorioSolicitudes repo = new RepositorioSolicitudes();
    SolicitudService service = new SolicitudService(repo, detectorSiempreTrue);

    SolicitudEliminacion solicitud = new SolicitudEliminacion("Mensaje sospechoso de spam", null);
    hecho.agregarSolicitud(solicitud);
    service.procesarNuevaSolicitud(solicitud, hecho);

    //reviso la solicitud desde el hecho
    SolicitudEliminacion solicitudDesdeHecho = hecho.getSolicitudes().get(0);

    assertFalse(solicitudDesdeHecho.estaPendiente(), "La solicitud debería haber sido rechazada por spam");
  }

  @Test
  public void solicitudQuedaPendienteSiNoEsSpam() {
    Hecho hecho = new HechoBuilder()
        .titulo("Hecho legítimo")
        .descripcion("Descripción normal")
        .categoria("ambiental")
        .latitud(10.0)
        .longitud(20.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .origen(Origen.CARGAMANUAL)
        .visible(true)
        .build();

    DetectorDeSpam detectorSiempreFalse = texto -> false;
    RepositorioSolicitudes repo = new RepositorioSolicitudes();
    SolicitudService service = new SolicitudService(repo, detectorSiempreFalse);

    SolicitudEliminacion solicitud = new SolicitudEliminacion("Texto valido y claro", null);

    hecho.agregarSolicitud(solicitud);
    service.procesarNuevaSolicitud(solicitud, hecho);

    //reviso la solicitud desde el hecho
    SolicitudEliminacion solicitudDesdeHecho = hecho.getSolicitudes().get(0);

    assertTrue(solicitudDesdeHecho.estaPendiente(), "La solicitud debería quedar pendiente si no es spam");
  }
}