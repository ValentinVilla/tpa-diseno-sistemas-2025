package db;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

public class ContextTest implements SimplePersistenceTest {
  private HechoBuilder crearHecho(String titulo) {
    return new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(-31.4167)
        .longitud(-64.1833)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
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
  void testPersistHecho() throws Exception {

    RepositorioHechos repo = RepositorioHechos.getInstancia();
    Hecho hecho = new Hecho(crearHecho("Prueba persistance"));

    repo.guardar(hecho);
  }

}