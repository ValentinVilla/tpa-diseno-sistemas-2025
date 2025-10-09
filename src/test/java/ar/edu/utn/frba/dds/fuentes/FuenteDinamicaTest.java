package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FuenteDinamicaTest {

  private FuenteDinamica fuente;

  @BeforeEach
  void init() {
    fuente = new FuenteDinamica();
  }

  private EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
  private EntityManager entityManager = emf.createEntityManager();

  private final Contribuyente juan = new Contribuyente("juan", "cito", 1127564192, "perez", 20);

  private HechoDinamico crearHecho(String titulo) {
    HechoBuilder hechoBase = new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE);
    return new HechoDinamico(hechoBase, juan);
  }

  @Test
  void puedeSubirHechoAnonimo() {
    HechoDinamico hecho = crearHecho("hecho-anonimo");
    fuente.subirHecho(hecho);

    assertEquals(1, fuente.cargarHechos(null).size());
  }


  @Test
  void puedeSolicitarModificarHechoDentroDelPlazo() {
    //necesito que juan este persistido para que el hecho lo tenga asociado
    entityManager.getTransaction().begin();
    entityManager.persist(juan);
    entityManager.flush();
    entityManager.getTransaction().commit();

    HechoDinamico original = crearHecho("original");
    HechoDinamico nuevo = crearHecho("incendio-modificado");
    FuenteDinamica fuente = new FuenteDinamica();

    //fuente.subirHecho(10, original);
    fuente.subirHecho(original);
    fuente.solicitarModificarHecho(original, nuevo, "motivo por el cual quiero realizar la modificacion");

    //se crea el nuevo modificado pero se pone en false
    assertFalse(nuevo.getVisible());
  }

  @Test
  void noPuedeSolicitarModificarHechoSiEsOtroUsuario() {
    HechoDinamico original = crearHecho("original");
    fuente.subirHecho(original);

    Contribuyente tomas = new Contribuyente("tomas","tomas", 1127587659, "hola@gmail.com", 18);

    HechoBuilder Builder = new HechoBuilder()
        .titulo("malicioso")
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);
    HechoDinamico nuevo = new HechoDinamico(Builder, tomas);

    assertThrows(RuntimeException.class, () -> fuente.solicitarModificarHecho(original, nuevo, "motivo por el cual quiero realizar la modificacion"));
  }

  @Test
  void noPuedeModificarHechoFueraDePlazo() {

    // Crear hecho con fecha de carga hace más de 7 días
    HechoBuilder Builder = new HechoBuilder()
        .titulo("original")
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now().minusDays(8))
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);
    HechoDinamico hechoOriginal = new HechoDinamico(Builder, juan);

    // Se sube el hecho
    fuente.subirHecho(hechoOriginal);

    HechoBuilder Builder2 = new HechoBuilder()
        .titulo("modificado")
        .descripcion("nueva desc")
        .categoria("cat")
        .latitud(2.0)
        .longitud(2.0)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);

    HechoDinamico hechoNuevo = new HechoDinamico(Builder2, juan);

    // Esperamos excepcion al intentar modificar fuera del plazo
    assertThrows(RuntimeException.class, () -> fuente.solicitarModificarHecho(hechoOriginal, hechoNuevo, "texto del motivo"));
  }
  @Test
  void puedeEliminarHecho() {
    HechoDinamico hecho = crearHecho("para borrar");
    fuente.subirHecho(hecho);

    fuente.eliminarHecho(hecho);

    assertFalse(hecho.getVisible());
  }
}
