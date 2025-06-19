package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
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
    return new HechoDinamico(hechoBase, new Contribuyente(42, 25,"juan", "perez"));
  }

  @Test
  void puedeSubirHechoAnonimo() {
    HechoDinamico hecho = crearHecho("hecho-anonimo");
    fuente.subirHecho(hecho);

    assertEquals(1, fuente.cargarHechos(null).size());
  }

  @Test
  void puedeSubirHechoRegistrado() {
    HechoDinamico hecho = crearHecho("hecho-registrado");
    fuente.subirHecho(hecho);

    assertEquals(42, hecho.getContribuyente().getId());
  }

  @Test
  void puedeSolicitarModificarHechoDentroDelPlazo() {
    HechoDinamico original = crearHecho("incendio");
    HechoDinamico nuevo = crearHecho("incendio-modificado");
    FuenteDinamica fuente = new FuenteDinamica(repo);

    //fuente.subirHecho(10, original);
    fuente.subirHecho(original);

    fuente.solicitarModificarHecho(original.getContribuyente().getId(), original, nuevo, "motivo por el cual quiero realizar la modificacion");

    assertTrue(nuevo.getVisible());
    assertFalse(original.getVisible());
  }

  @Test
  void noPuedeSolicitarModificarHechoSiEsOtroUsuario() {
    HechoDinamico original = crearHecho("original");
    fuente.subirHecho(original);

    HechoDinamico nuevo = crearHecho("malicioso");

    assertThrows(RuntimeException.class, () -> fuente.solicitarModificarHecho(99, original, nuevo, "motivo por el cual quiero realizar la modificacion"));
  }

  @Test
  void noPuedeSolicitarModificarHechoFueraDePlazo() {
    Contribuyente contribuyente = new Contribuyente(42, 25,"juan", "perez");

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
    HechoDinamico hecho = new HechoDinamico(Builder, contribuyente);

    // Se sube el hecho
    fuente.subirHecho(hecho);

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

    HechoDinamico datosNuevos = new HechoDinamico(Builder2, contribuyente);

    // Esperamos excepcion al intentar modificar fuera del plazo
    assertThrows(RuntimeException.class, () -> fuente.solicitarModificarHecho(contribuyente.getId(), hecho, datosNuevos, "texto del motivo"));
  }

  @Test
  void puedeEliminarHecho() {
    HechoDinamico hecho = crearHecho("para borrar");
    fuente.subirHecho(hecho);

    repo.eliminar(hecho);

    assertFalse(hecho.getVisible());
  }
}
