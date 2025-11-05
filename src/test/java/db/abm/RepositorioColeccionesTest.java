package db.abm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.consenso.ModoNavegacion;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class RepositorioColeccionesTest {

  private RepositorioColecciones repo;

  @BeforeEach
  void setUp() {
    repo = RepositorioColecciones.getInstancia();
  }

  private Coleccion crearColeccion(String titulo) {
    ArrayList<String> campos = new ArrayList<>(List.of(
        "titulo", "descripcion", "latitud", "longitud", "fechaHecho"
    ));

    return new ColeccionBuilder()
        .titulo(titulo)
        .descripcion("Ahora si: Probando el detalle de colecciones, amo programar y amo mi vida!! Noche soñada de sabado se viene hoy. +1000 lineas, bueno creo que ya es bastante larga la descripcion como para que se aprecie bien como se ve.")
        .fuente(new FuenteEstatica("/Users/gongarfon/DDS/tpa-2025-04/Diagramas/hechos_argentina.csv", "testing", campos))
        .criterio(new FiltroCategoria("testing"))
        .handle("handle-" + titulo)
        .modoNavegacion(ModoNavegacion.IRRESTRICTA)
        .algoritmoConsenso(AlgoritmoConsenso.ABSOLUTO)
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
