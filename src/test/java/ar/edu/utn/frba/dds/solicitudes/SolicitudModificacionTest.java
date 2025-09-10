package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitudModificacionTest {
  private HechoDinamico hechoOriginal;
  private HechoDinamico hechoModificado;
  private SolicitudModificacion solicitud;
  private FuenteDinamica fuente;
  private Contribuyente contribuyente;

  @BeforeEach
  void setUp() {
    fuente = new FuenteDinamica();
    HechoBuilder builderOriginal = new HechoBuilder()
        .titulo("Hecho original")
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);

    HechoBuilder builderModificado = new HechoBuilder()
        .titulo("Hecho modificado")
        .descripcion("nueva desc")
        .categoria("cat")
        .latitud(1.5)
        .longitud(1.5)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);

    contribuyente = new Contribuyente(42, "juan", "perez");
    hechoOriginal = new HechoDinamico(builderOriginal, contribuyente);
    hechoModificado = new HechoDinamico(builderModificado, contribuyente);
    solicitud = new SolicitudModificacion(hechoOriginal, "sugerenciaModificacion",new ImplementadorSpam(10),hechoModificado);
  }

   @Test
  void aplicarRechazoDejaALaSolicitudRechazadaYLosHechosComoEstan() {
    solicitud.rechazar();

    assertEquals(solicitud.estado, EstadoSolicitud.RECHAZADA);
    assertTrue(hechoOriginal.getVisible());
    assertFalse(hechoModificado.getVisible());
  }

  @Test
  void aceptarConSugerenciaGuardaSugerencia() {
    solicitud.aceptarConSugerencia("Mejorar ubicación");

    assertEquals("Mejorar ubicación", solicitud.getTextoFundamentacion());
  }

}

