package ar.edu.utn.frba.dds.solicitudes;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
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
        .visible(true)
        .build();

    DetectorDeSpam detectorSiempreTrue = texto -> true;

    assertThrows(IllegalArgumentException.class, () -> {
      new SolicitudEliminacion("Mensaje sospechoso de spam", hecho, detectorSiempreTrue);
    }, "Se esperaba una excepción por mensaje detectado como spam");
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
        .visible(true)
        .build();

    DetectorDeSpam detectorSiempreFalse = texto -> false;

    SolicitudEliminacion solicitud = new SolicitudEliminacion("Texto valido y claro", hecho, detectorSiempreFalse);

    hecho.agregarSolicitud(solicitud);

    //reviso la solicitud desde el hecho
    SolicitudEliminacion solicitudDesdeHecho = hecho.getSolicitudes().get(0);

    assertTrue(solicitudDesdeHecho.estaPendiente(), "La solicitud debería quedar pendiente si no es spam");
  }
}