package db;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

public class ContextTest implements SimplePersistenceTest {
  private HechoBuilder crearHecho(String titulo) {
    return new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);
  }

  @Test
  void contextUp() {
    assertNotNull(entityManager());
  }

  @Test
  void contextUpWithTransaction() throws Exception {
    withTransaction(() -> {
    });
  }

  @Test
  void testPersistHecho() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    EntityManager em = emf.createEntityManager();

    em.getTransaction().begin();

    Hecho hecho = new Hecho(crearHecho("Prueba persistance"));
    em.persist(hecho);

    em.getTransaction().commit();
    em.close();
    emf.close();
  }

}