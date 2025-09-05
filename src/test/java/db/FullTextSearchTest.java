package db;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FullTextSearchTest {

  private static EntityManagerFactory emf;
  private EntityManager em;
  private RepositorioHechos repositorioHechos;

  @BeforeAll
  static void initFactory() {
    emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
  }

  @AfterAll
  static void closeFactory() {
    if (emf != null) {
      emf.close();
    }
  }

  @BeforeEach
  void init() {
    em = emf.createEntityManager();
    repositorioHechos = new RepositorioHechos(em);
  }

  @AfterEach
  void close() {
    if (em != null) {
      em.close();
    }
  }

  private Hecho buildHecho(String titulo, String descripcion, String categoria) {
    return new HechoBuilder()
        .titulo(titulo)
        .descripcion(descripcion)
        .categoria(categoria)
        .latitud(0.0)
        .longitud(0.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .origen(Origen.CONTRIBUYENTE)
        .visible(true)
        .build();
  }

  @Test
  void testBuscarPorTextoOrdenaPorRelevancia() {
    // Arrange
    Hecho hecho1 = buildHecho("Guerra y Revolución Industrial", "Descripción sobre el conflicto bélico.", "Historia");
    Hecho hecho2 = buildHecho("La Guerra del Pacífico", "Un conflicto bélico de la historia.", "Revolución");
    Hecho hecho3 = buildHecho("Femicidio", "El concepto de desconstrucción social.", "Sociedad");

    repositorioHechos.guardar(hecho1);
    repositorioHechos.guardar(hecho2);
    repositorioHechos.guardar(hecho3);

    List<Hecho> resultados = repositorioHechos.buscarPorTextoEnDB("guerra & revolución");

    assertFalse(resultados.isEmpty());
    assertEquals(2, resultados.size());
    assertEquals("Guerra y Revolución Industrial", resultados.get(0).getTitulo());
    assertEquals("La Guerra del Pacífico", resultados.get(1).getTitulo());
    }
}
