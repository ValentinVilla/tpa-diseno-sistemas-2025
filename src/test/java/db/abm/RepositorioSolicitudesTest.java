package db.abm;

import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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
    lionel = new Contribuyente(38, "Lionel", "Messi");
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
  void testGuardarSolicitud() throws Exception {
    HechoDinamico hecho = crearHechoDinamico();

    //solo para testear
    entityManager.getTransaction().begin();
    entityManager.persist(hecho);
    entityManager.getTransaction().commit();

    Solicitud solicitud = new SolicitudEliminacion(hecho, "Prueba de guardado", null);
    repo.guardar(solicitud);

    assertTrue(repo.obtenerTodas().stream()
        .anyMatch(s -> s.getId().equals(solicitud.getId())));
  }

  @Test
  void testEliminarSolicitud() throws Exception {
    HechoDinamico hecho = crearHechoDinamico();
    entityManager.getTransaction().begin();
    entityManager.persist(hecho);
    entityManager.getTransaction().commit();

    Solicitud solicitud = new SolicitudEliminacion(hecho, "Prueba de eliminación", null);
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

    Solicitud solicitud = new SolicitudEliminacion(hecho, "Prueba de actualización", null);
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