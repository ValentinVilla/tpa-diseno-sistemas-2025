package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitudSubidaTest {

  private HechoDinamico hecho;
  private SolicitudSubida solicitud;

  @BeforeEach
  void setUp() {
    hecho = new HechoDinamico(new HechoBuilder(), new Contribuyente(42, "juan", "perez"));
    solicitud = new SolicitudSubida(hecho, "motivo de subida", new ImplementadorSpam(10));
  }
  // PARA QUE ESTOS TEST TENGAN SENTIDO PODRIAMOS COMPARAR SI EL HECHO EXISTE EN HECHOS ELIMINADOS ANTES Y DESPUES DE LA SOLICITUD

  @Test
  void testAplicarRechazoMarcaHechoComoNoVisible() {
    assertTrue(hecho.getVisible(), "El hecho debería ser visible inicialmente");

    assertFalse(hecho.getVisible(), "El hecho debería quedar no visible tras el rechazo");
  }

  @Test
  void testAplicarAceptacionNoModificaVisibilidad() {
    assertTrue(hecho.getVisible());

    solicitud.aplicarAceptacion();

    assertTrue(hecho.getVisible(), "La visibilidad no debería cambiar con aceptación directa");
  }
}
