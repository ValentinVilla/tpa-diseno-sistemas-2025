package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FuenteDinamicaTest {

  private FuenteDinamica fuente;
  private RepositorioHechos repo;

  @BeforeEach
  void init() {
    repo = new RepositorioHechos();
    fuente = new FuenteDinamica("fuente-test", repo);
  }

  private Hecho crearHecho(String titulo) {
    return new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE)
        .build();
  }

  @Test
  void puedeSubirHechoAnonimo() {
    Hecho hecho = crearHecho("hecho-anonimo");
    fuente.subirHecho(hecho);

    assertEquals(1, fuente.cargarHechos().size());
  }

  @Test
  void puedeSubirHechoRegistrado() {
    Hecho hecho = crearHecho("hecho-registrado");
    fuente.subirHecho(42, hecho);

    assertEquals(42, hecho.getIdContribuyenteCreador());
  }

  @Test
  void puedeModificarHechoDentroDelPlazo() {
    Hecho original = crearHecho("original");
    fuente.subirHecho(1, original);

    Hecho nuevo = crearHecho("modificado");
    fuente.modificarHecho(1, original, nuevo);

    assertEquals("modificado", original.getTitulo());
  }

  @Test
  void noPuedeModificarHechoSiEsOtroUsuario() {
    Hecho original = crearHecho("original");
    fuente.subirHecho(1, original);

    Hecho nuevo = crearHecho("malicioso");

    assertThrows(RuntimeException.class, () -> fuente.modificarHecho(99, original, nuevo));
  }

  @Test
  void noPuedeModificarHechoFueraDePlazo() {
    int idContribuyente = 1;

    // Crear hecho con fecha de carga hace más de 7 días
    Hecho hecho = new HechoBuilder()
        .titulo("original")
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now().minusDays(8)) // Simula pasaron 8 dias
        .visible(true)
        .origen(Origen.CONTRIBUYENTE)
        .build();

    // Se sube el hecho
    fuente.subirHecho(idContribuyente, hecho);

    Hecho datosNuevos = new HechoBuilder()
        .titulo("modificado")
        .descripcion("nueva desc")
        .categoria("cat")
        .latitud(2.0)
        .longitud(2.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now()) // no importa
        .visible(true)
        .origen(Origen.CONTRIBUYENTE)
        .build();

    // Esperamos excepcion al intentar modificar fuera del plazo
    assertThrows(RuntimeException.class, () -> fuente.modificarHecho(1, hecho, datosNuevos));
  }

  @Test
  void puedeEliminarHecho() {
    Hecho hecho = crearHecho("para borrar");
    fuente.subirHecho(1, hecho);

    repo.eliminar(hecho);

    assertTrue(repo.obtenerhechosDe(fuente).isEmpty());
  }
}
