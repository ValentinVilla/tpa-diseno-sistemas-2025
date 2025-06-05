package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoContribuyente;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitudModificacionTest {
  private HechoContribuyente hechoOriginal;
  private HechoContribuyente hechoModificado;
  private SolicitudModificacion solicitud;
  private FuenteDinamica fuente;

  @BeforeEach
  void setUp() {
    fuente = new FuenteDinamica(new RepositorioHechos());
    HechoBuilder builderOriginal = new HechoBuilder()
        .titulo("Hecho original")
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);

    HechoBuilder builderModificado = new HechoBuilder()
        .titulo("Hecho modificado")
        .descripcion("nueva desc")
        .categoria("cat")
        .latitud(1.5)
        .longitud(1.5)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);

    hechoOriginal = new HechoContribuyente(builderOriginal);
    hechoModificado = new HechoContribuyente(builderModificado);
    solicitud = new SolicitudModificacion(hechoOriginal, hechoModificado);
  }

   @Test
  void aplicarRechazoRevierteVisibilidad() {
    solicitud.aplicarRechazo();

    assertTrue(hechoOriginal.getVisible());
    assertFalse(hechoModificado.getVisible());
  }

  @Test
  void aceptarConSugerenciaGuardaSugerencia() {
    solicitud.aceptarConSugerencia("Mejorar ubicación");

    assertEquals("Mejorar ubicación", solicitud.getSugerenciaModificacion());
    assertFalse(hechoOriginal.getVisible());
    assertTrue(hechoModificado.getVisible());
  }

}

