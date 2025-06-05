package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.HechoContribuyente;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitudSubidaTest {

  private HechoContribuyente hecho;
  private SolicitudSubida solicitud;

  @BeforeEach
  void setUp() {
    hecho = new HechoContribuyente(new HechoBuilder());
    hecho.setVisible(true);
    solicitud = new SolicitudSubida(hecho);
  }

  @Test
  void testAceptarConSugerenciaGuardaTextoCorrectamente() {
    solicitud.aceptarConSugerencia("Agregar fuente de datos oficial");

    assertEquals("Agregar fuente de datos oficial", solicitud.getSugerenciaModificacion());

  }

  @Test
  void testAplicarRechazoMarcaHechoComoNoVisible() {
    assertTrue(hecho.getVisible(), "El hecho debería ser visible inicialmente");

    solicitud.aplicarRechazo();

    assertFalse(hecho.getVisible(), "El hecho debería quedar no visible tras el rechazo");
  }

  @Test
  void testAplicarAceptacionNoModificaVisibilidad() {
    assertTrue(hecho.getVisible());

    solicitud.aplicarAceptacion();

    assertTrue(hecho.getVisible(), "La visibilidad no debería cambiar con aceptación directa");
  }
}
