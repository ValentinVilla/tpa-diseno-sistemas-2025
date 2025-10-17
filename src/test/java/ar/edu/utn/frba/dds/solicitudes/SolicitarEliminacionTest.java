package ar.edu.utn.frba.dds.solicitudes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class SolicitarEliminacionTest {


  @Test
  public void solicitudEsRechazadaAutomaticamenteSiEsSpam() {
    Hecho hecho = new HechoBuilder()
        .titulo("Spam Title")
        .descripcion("Spam description")
        .categoria("fake-news")
        .latitud(0.0)
        .longitud(0.0)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CARGAMANUAL)
        .build();

    DetectorDeSpam detectorSiempreTrue = texto -> true;
    SolicitudEliminacion solicitudEliminacion = new SolicitudEliminacion(hecho ,"spam", detectorSiempreTrue);

    assertEquals(EstadoSolicitud.RECHAZADA, solicitudEliminacion.getEstado());
  }

  @Test
  public void solicitudQuedaPendienteSiNoEsSpam() {
    Hecho hecho = new HechoBuilder()
        .titulo("Hecho legítimo")
        .descripcion("Descripción normal")
        .categoria("ambiental")
        .latitud(10.0)
        .longitud(20.0)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CARGAMANUAL)
        .build();

    DetectorDeSpam detectorSiempreFalse = texto -> false;

    SolicitudEliminacion solicitud = new SolicitudEliminacion( hecho ,"Texto valido y claro", detectorSiempreFalse);

    assertTrue(solicitud.estaPendiente(), "La solicitud debería quedar pendiente si no es spam");
  }
}