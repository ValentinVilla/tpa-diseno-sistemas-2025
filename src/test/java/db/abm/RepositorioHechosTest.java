package db.abm;

import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class RepositorioHechosTest {

  private RepositorioHechos repo;

  @BeforeEach
  void setUp() {
    repo = RepositorioHechos.getInstancia();
  }

  private Hecho crearHecho(String titulo) {
    return new HechoBuilder()
        .titulo(titulo)
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
  void testGuardarHecho() throws Exception {
    Hecho hecho = crearHecho("Hecho Test");
    repo.guardar(hecho);

    assertTrue(repo.buscarPorTextoEnDB("Hecho Test").stream()
        .anyMatch(h -> h.getId() == hecho.getId()));
  }

  @Test
  void testEliminarHecho() throws Exception {
    Hecho hecho = crearHecho("Hecho eliminar");
    repo.guardar(hecho);

    repo.eliminar(hecho.getId());
    assertTrue(repo.buscarPorTextoEnDB("Hecho eliminar").isEmpty());
  }

  @Test
  void testActualizarHecho() throws Exception {
    Hecho hecho = crearHecho("Hecho original");
    repo.guardar(hecho);

    hecho.setTitulo("Hecho modificado");
    repo.actualizar(hecho);

    assertTrue(repo.buscarPorTextoEnDB("modificado").stream()
        .anyMatch(h -> h.getId() == hecho.getId()));
  }
}