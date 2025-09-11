package db.abm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import ar.edu.utn.frba.dds.consenso.ConsensoAbsoluto;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.ModoNavegacion;
import ar.edu.utn.frba.dds.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepositorioColeccionesTest {

  private RepositorioColecciones repo;

  @BeforeEach
  void setUp() {
    repo = RepositorioColecciones.getInstancia();
  }

  private Coleccion crearColeccion(String titulo) {
    return new ColeccionBuilder()
        .titulo(titulo)
        .descripcion("Descripción de prueba")
        .fuente(new FuenteDinamica())
        .criterio(new FiltroCategoria())
        .handle("handle-" + titulo)
        .modoNavegacion(ModoNavegacion.IRRESTRICTA)
        .algoritmoConsenso(new ConsensoAbsoluto())
        .build();
  }

  @Test
  void testGuardarColeccion() {
    Coleccion coleccion = crearColeccion("Coleccion Test");
    repo.guardar(coleccion);

    assertNotNull(repo.buscarPorID(coleccion.getId()));
  }

  @Test
  void testEliminarColeccion() {
    Coleccion coleccion = crearColeccion("Coleccion eliminar");
    repo.guardar(coleccion);

    repo.eliminar(coleccion.getId());
    assertNull(repo.buscarPorID(coleccion.getId()));
  }

  @Test
  void testActualizarColeccion() {
    Coleccion coleccion = crearColeccion("Coleccion original");
    repo.guardar(coleccion);

    coleccion.setTitulo("Coleccion modificada");
    repo.actualizar(coleccion);

    Coleccion updated = repo.buscarPorID(coleccion.getId());
    assertEquals("Coleccion modificada", updated.getTitulo());
  }
}
