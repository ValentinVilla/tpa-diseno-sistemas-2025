package db;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.servicios.HechoFTS;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FullTextSearchTest {

  private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");;
  private EntityManager entityManager = emf.createEntityManager();
  private DAOHechos DAOhechos;
  private HechoFTS hechoFTS;
  private FuenteDinamica fuenteDinamica = new FuenteDinamica();
  private Contribuyente juan = new Contribuyente(40,"juan", "perez");
  @BeforeEach
  void init() {
      hechoFTS = new HechoFTS(DAOhechos);

      Hecho hecho1 = buildHecho("Guerra y Revolución Industrial", "Descripción sobre el conflicto bélico.", "Historia");
      Hecho hecho2 = buildHecho("La Guerra del Pacífico", "Un conflicto bélico de la historia.", "Revolución");
      Hecho hecho3 = buildHecho("Femicidio", "El concepto de desconstrucción social.", "Sociedad");
      Hecho hecho4 = buildHecho("Accidente de tránsito", "Choque en la ruta 9", "Transito");
      Hecho hecho5 = buildHecho("Accidente laboral", "Herido en la fábrica", "Trabajo");

      entityManager.getTransaction().begin();
      entityManager.persist(hecho1);
      entityManager.persist(hecho2);
      entityManager.persist(hecho3);
      entityManager.persist(hecho4);
      entityManager.persist(hecho5);
      entityManager.flush();
      entityManager.getTransaction().commit();
}

  @AfterEach
  void close() {
    if (entityManager != null) {
      entityManager.close();
    }
  }

  private Hecho buildHecho(String titulo, String descripcion, String categoria) {
    return new HechoBuilder()
        .titulo(titulo)
        .descripcion(descripcion)
        .categoria(categoria)
        .latitud(-31.4167)
        .longitud(-64.1833)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE)
        .build();
  }

  @Test
  void testBuscarPorTextoOrdenaPorRelevancia() throws Exception {
    entityManager.getTransaction().begin();
    entityManager.persist(fuenteDinamica);
    entityManager.persist(juan);

    Hecho hecho1 = buildHecho("Guerra y Revolución Industrial", "Descripción sobre el conflicto bélico.", "Historia");
    Hecho hecho2 = buildHecho("La Guerra del Pacífico", "Un conflicto bélico de la historia.", "Revolución");
    Hecho hecho3 = buildHecho("Femicidio", "El concepto de desconstrucción social.", "Sociedad");

    repositorioHechos.guardar(hecho1);
    repositorioHechos.guardar(hecho2);
    repositorioHechos.guardar(hecho3);

    List<Hecho> resultados = hechoFTS.buscar("guerra y revolución");

    assertFalse(resultados.isEmpty());
    assertEquals("Guerra y Revolución Industrial", resultados.get(0).getTitulo());
    }

  @Test
  void testBuscarPorTextoConFaltaDeOrtografia() throws Exception {
    List<Hecho> resultados = hechoFTS.buscar("acisdente");

    assertFalse(resultados.isEmpty());
    assertTrue(resultados.stream().anyMatch(h -> h.getTitulo().equals("Accidente de tránsito")));
    assertTrue(resultados.stream().anyMatch(h -> h.getTitulo().equals("Accidente laboral")));
  }
}
