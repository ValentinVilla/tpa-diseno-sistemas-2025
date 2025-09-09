package db;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.servicios.HechoService;
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
  private HechoService hechoService;

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
    repositorioHechos = RepositorioHechos.getInstancia();
    hechoService = new HechoService(repositorioHechos);
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
        .latitud(-31.4167)
        .longitud(-64.1833)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .origen(Origen.CONTRIBUYENTE)
        .visible(true)
        .build();
  }

  @Test
  void testBuscarPorTextoOrdenaPorRelevancia() throws Exception {
    Hecho hecho1 = buildHecho("Guerra y Revolución Industrial", "Descripción sobre el conflicto bélico.", "Historia");
    Hecho hecho2 = buildHecho("La Guerra del Pacífico", "Un conflicto bélico de la historia.", "Revolución");
    Hecho hecho3 = buildHecho("Femicidio", "El concepto de desconstrucción social.", "Sociedad");

    repositorioHechos.guardar(hecho1);
    repositorioHechos.guardar(hecho2);
    repositorioHechos.guardar(hecho3);

    List<Hecho> resultados = hechoService.buscar("guerra y revolución");

    assertFalse(resultados.isEmpty());
    assertEquals("Guerra y Revolución Industrial", resultados.get(0).getTitulo());
    assertEquals("La Guerra del Pacífico", resultados.get(1).getTitulo());
    }

  @Test
  void testBuscarPorTextoConFaltaDeOrtografia() throws Exception {
    Hecho hecho1 = buildHecho("Accidente de tránsito", "Choque en la ruta 9", "Transito");
    Hecho hecho2 = buildHecho("Accidente laboral", "Herido en la fábrica", "Trabajo");

    repositorioHechos.guardar(hecho1);
    repositorioHechos.guardar(hecho2);

    List<Hecho> resultados = hechoService.buscar("acisdente");

    assertFalse(resultados.isEmpty());
    assertTrue(resultados.stream().anyMatch(h -> h.getTitulo().equals("Accidente de tránsito")));
    assertTrue(resultados.stream().anyMatch(h -> h.getTitulo().equals("Accidente laboral")));
  }
}
