package db.abm;

import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudSubida;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


class RepositorioSolicitudesTest {

  private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
  private EntityManager entityManager;
  private RepositorioSolicitudes repo;

  private Contribuyente lionel;

  @BeforeEach
  void setUp() {
    entityManager = emf.createEntityManager();
    repo = RepositorioSolicitudes.getInstancia();

    // 2. Creamos y persistimos el Contribuyente ANTES de cada prueba
    lionel = new Contribuyente("Gonzalo", "Garcia", "23453253", "asd@gmail.com", 18, "Messi");
    entityManager.getTransaction().begin();
    entityManager.persist(lionel);
    entityManager.getTransaction().commit();
  }

  @AfterEach
  void close() {
    if (entityManager != null) {
      entityManager.close();
    }
  }

  private HechoDinamico crearHechoDinamico() {
    HechoBuilder builder = new HechoBuilder()
        .titulo("Hecho Dinamico para Solicitud")
        .descripcion("descripcion random")
        .categoria("categoria random")
        .latitud(-31.4167)
        .longitud(-64.1833)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE);
    return new HechoDinamico(builder, lionel);
  }

  @Test
  void testGuardarSolicitudEliminacion() throws Exception {
    HechoDinamico hecho = crearHechoDinamico();

    //solo para testear
    entityManager.getTransaction().begin();
    entityManager.persist(hecho);
    entityManager.getTransaction().commit();

    Solicitud solicitud = new SolicitudEliminacion(hecho, "Prueba de guardado", null,  new Contribuyente("juan","","","",11,""));
    repo.guardar(solicitud);

    assertTrue(repo.obtenerTodas().stream()
        .anyMatch(s -> s.getId().equals(solicitud.getId())));
  }

  @Test
  void testGuardarSolicitudSubida() throws Exception {
    HechoDinamico hecho = crearHechoDinamico();

    //solo para testear
    entityManager.getTransaction().begin();
    entityManager.persist(hecho);
    entityManager.getTransaction().commit();

    Solicitud solicitud = new SolicitudSubida(hecho, "Prueba de guardado", null,  new Contribuyente("juan","","","",11,""));
    repo.guardar(solicitud);

    assertTrue(repo.obtenerTodas().stream()
        .anyMatch(s -> s.getId().equals(solicitud.getId())));
  }

  /*@Test
  void testGuardarSolicitudModificacion() throws Exception {
    HechoDinamico hecho = crearHechoDinamico();

    //solo para testear
    entityManager.getTransaction().begin();
    entityManager.persist(hecho);
    entityManager.getTransaction().commit();

    Solicitud solicitud = new SolicitudModificacion(hecho, "Prueba de guardado", null, (aca falta algo para que ande)  ,new Contribuyente("juan","","","",11,""));
    repo.guardar(solicitud);

    assertTrue(repo.obtenerTodas().stream()
        .anyMatch(s -> s.getId().equals(solicitud.getId())));
  }*/

  @Test
  void testEliminarSolicitud() throws Exception {
    HechoDinamico hecho = crearHechoDinamico();
    entityManager.getTransaction().begin();
    entityManager.persist(hecho);
    entityManager.getTransaction().commit();

    Solicitud solicitud = new SolicitudEliminacion(hecho, "Prueba de eliminación", null,  new Contribuyente("juan","","","",11,""));
    repo.guardar(solicitud);

    repo.eliminar(solicitud.getId());

    assertFalse(repo.obtenerTodas().stream()
        .anyMatch(s -> s.getId().equals(solicitud.getId())));
  }



  @Test
  void testActualizarSolicitud() throws Exception {
    HechoDinamico hecho = crearHechoDinamico();
    entityManager.getTransaction().begin();
    entityManager.persist(hecho);
    entityManager.getTransaction().commit();

    Solicitud solicitud = new SolicitudEliminacion(hecho, "Prueba de actualización", null,  new Contribuyente("juan","","","",11,""));
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