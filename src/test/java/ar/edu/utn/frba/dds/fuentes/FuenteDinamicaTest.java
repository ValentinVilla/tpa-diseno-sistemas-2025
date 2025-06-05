package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.HechoContribuyente;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
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
    fuente = new FuenteDinamica(repo);
  }

  private HechoContribuyente crearHecho(String titulo) {
    HechoBuilder hechoBase = new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);
    return new HechoContribuyente(hechoBase);
  }

  @Test
  void puedeSubirHechoAnonimo() {
    HechoContribuyente hecho = crearHecho("hecho-anonimo");
    fuente.subirHecho(-1, hecho);

    assertEquals(1, fuente.cargarHechos().size());
  }

  @Test
  void puedeSubirHechoRegistrado() {
    HechoContribuyente hecho = crearHecho("hecho-registrado");
    fuente.subirHecho(42, hecho);

    assertEquals(42, hecho.getIdContribuyenteCreador());
  }

  @Test
  void puedeModificarHechoDentroDelPlazo() {
    HechoContribuyente original = crearHecho("incendio");
    HechoContribuyente nuevo = crearHecho("incendio-modificado");
    FuenteDinamica fuente = new FuenteDinamica(repo);

    //fuente.subirHecho(10, original);
    fuente.subirHecho(10, original);

    fuente.modificarHecho(10, original, nuevo);

    assertTrue(nuevo.getVisible());
    assertFalse(original.getVisible());
  }

  @Test
  void noPuedeModificarHechoSiEsOtroUsuario() {
    HechoContribuyente original = crearHecho("original");
    fuente.subirHecho(1, original);

    HechoContribuyente nuevo = crearHecho("malicioso");

    assertThrows(RuntimeException.class, () -> fuente.modificarHecho(99, original, nuevo));
  }

  @Test
  void noPuedeModificarHechoFueraDePlazo() {
    int idContribuyente = 1;

    // Crear hecho con fecha de carga hace más de 7 días
    HechoBuilder Builder = new HechoBuilder()
        .titulo("original")
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now().minusDays(8)) // Simula pasaron 8 dias
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);
    HechoContribuyente hecho = new HechoContribuyente(Builder);

    // Se sube el hecho
    fuente.subirHecho(idContribuyente, hecho);

    HechoBuilder Builder2 = new HechoBuilder()
        .titulo("modificado")
        .descripcion("nueva desc")
        .categoria("cat")
        .latitud(2.0)
        .longitud(2.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now()) // no importa
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);

    HechoContribuyente datosNuevos = new HechoContribuyente(Builder2);

    // Esperamos excepcion al intentar modificar fuera del plazo
    assertThrows(RuntimeException.class, () -> fuente.modificarHecho(1, hecho, datosNuevos));
  }

  @Test
  void puedeEliminarHecho() {
    HechoContribuyente hecho = crearHecho("para borrar");
    fuente.subirHecho(1, hecho);

    repo.eliminar(hecho);

    assertFalse(hecho.getVisible());
  }
}
