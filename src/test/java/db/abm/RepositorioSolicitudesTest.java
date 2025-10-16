package db.abm;

import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class RepositorioSolicitudesTest {

  private RepositorioSolicitudes repo;
  private Hecho hecho = crearHecho();
  private Solicitud solicitud = new SolicitudEliminacion(hecho, "Prueba solicitudes", null);

  @BeforeEach
  void setUp() {
    repo = RepositorioSolicitudes.getInstancia();
  }

  private Hecho crearHecho() {
    return new HechoBuilder()
        .titulo("Hecho Solicitud")
        .descripcion("desc")
        .categoria("cat")
        .latitud(-31.4167)
        .longitud(-64.1833)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE)
        .build();
  }

  @Test
  void testGuardarSolicitud() throws Exception {
    Hecho hecho = crearHecho();
    DAOHechos.getInstancia().guardar(hecho);

    Solicitud solicitud = new SolicitudEliminacion("Prueba", hecho, null);
    repo.guardar(solicitud);

    assertTrue(repo.obtenerTodas().stream()
        .anyMatch(s -> s.getId().equals(solicitud.getId())));
  }

  @Test
  void testEliminarSolicitud() throws Exception {
    Hecho hecho = crearHecho();
    DAOHechos.getInstancia().guardar(hecho);

    Solicitud solicitud = new SolicitudEliminacion("Prueba", hecho, null);
    repo.guardar(solicitud);

    repo.eliminar(solicitud.getId());
    assertFalse(repo.obtenerTodas().stream()
        .anyMatch(s -> s.getId().equals(solicitud.getId())));
  }

  @Test
  void testActualizarSolicitud() throws Exception {
    Hecho hecho = crearHecho();
    DAOHechos.getInstancia().guardar(hecho);

    Solicitud solicitud = new SolicitudEliminacion("Prueba", hecho, null);
    repo.guardar(solicitud);

    solicitud.aceptar();
    repo.actualizar(solicitud);

    Solicitud updated = repo.obtenerTodas().stream()
        .filter(s -> s.getId().equals(solicitud.getId()))
        .findFirst().orElse(null);

    assertNotNull(updated);
    assertEquals("ACEPTADA", updated.getEstado().name());
  }
}