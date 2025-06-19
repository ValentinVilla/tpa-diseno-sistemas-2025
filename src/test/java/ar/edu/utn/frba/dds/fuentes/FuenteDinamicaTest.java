package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.HechoDinamico;
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

  private HechoDinamico crearHecho(String titulo) {
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
    return new HechoDinamico(hechoBase);
  }

  @Test
  void puedeSubirHechoAnonimo() {
    HechoDinamico hecho = crearHecho("hecho-anonimo");
    fuente.subirHecho(-1, hecho);

    assertEquals(1, fuente.cargarHechos().size());
  }

  @Test
  void puedeSubirHechoRegistrado() {
    HechoDinamico hecho = crearHecho("hecho-registrado");
    fuente.subirHecho(42, hecho);

    assertEquals(42, hecho.getIdContribuyenteCreador());
  }

  @Test
  void puedeModificarHechoDentroDelPlazo() {
    HechoDinamico original = crearHecho("incendio");
    HechoDinamico nuevo = crearHecho("incendio-modificado");
    FuenteDinamica fuente = new FuenteDinamica(repo);

    //fuente.subirHecho(10, original);
    fuente.subirHecho(10, original);

    fuente.modificarHecho(10, original, nuevo);

    assertTrue(nuevo.getVisible());
    assertFalse(original.getVisible());
  }

  @Test
  void noPuedeModificarHechoSiEsOtroUsuario() {
    HechoDinamico original = crearHecho("original");
    fuente.subirHecho(1, original);

    HechoDinamico nuevo = crearHecho("malicioso");

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
    HechoDinamico hecho = new HechoDinamico(Builder);

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

    HechoDinamico datosNuevos = new HechoDinamico(Builder2);

    // Esperamos excepcion al intentar modificar fuera del plazo
    assertThrows(RuntimeException.class, () -> fuente.modificarHecho(1, hecho, datosNuevos));
  }

  @Test
  void puedeEliminarHecho() {
    HechoDinamico hecho = crearHecho("para borrar");
    fuente.subirHecho(1, hecho);

    repo.eliminar(hecho);

    assertFalse(hecho.getVisible());
  }
}
