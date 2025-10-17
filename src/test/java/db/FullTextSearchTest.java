package db;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.servicios.HechoFTS;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FullTextSearchTest {

  private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");  private EntityManager entityManager;
  private DAOHechos daoHechos;
  private HechoFTS hechoFTS;

  @BeforeAll
  static void setUp() {
      EntityManager entityManager = emf.createEntityManager();

      FuenteDinamica fuenteDinamica = new FuenteDinamica();
      Contribuyente juan = new Contribuyente(42, "Juan Martin", "DelPotro");

      entityManager.getTransaction().begin();
      entityManager.persist(juan);
      entityManager.persist(fuenteDinamica);

      fuenteDinamica.subirHecho(buildHechoDinamico("Guerra y Revolución Industrial", "Descripción sobre el conflicto bélico.", "Historia", juan));
      fuenteDinamica.subirHecho(buildHechoDinamico("La Guerra del Pacífico", "Un conflicto bélico de la historia.", "Revolución", juan));
      fuenteDinamica.subirHecho(buildHechoDinamico("Femicidio", "El concepto de desconstrucción social.", "Sociedad", juan));
      fuenteDinamica.subirHecho(buildHechoDinamico("Accidente de tránsito", "Choque en la ruta 9", "Transito", juan));
      fuenteDinamica.subirHecho(buildHechoDinamico("Accidente laboral", "Herido en la fábrica", "Trabajo" , juan));

      entityManager.getTransaction().commit();
      entityManager.close();
  }
  @BeforeEach
  void init() {
    daoHechos = DAOHechos.getInstancia();
    hechoFTS = new HechoFTS(daoHechos);
  }

  @AfterEach
  void close() {
    if (entityManager != null) {
      entityManager.close();
    }
  }

  private static HechoDinamico buildHechoDinamico(String titulo, String descripcion, String categoria, Contribuyente contribuyente) {
    HechoBuilder builder = new HechoBuilder()
        .titulo(titulo)
        .descripcion(descripcion)
        .categoria(categoria)
        .latitud(-31.4167)
        .longitud(-64.1833)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE);
    return new HechoDinamico(builder, contribuyente);
  }

  @Test
  void testBuscarPorTextoOrdenaPorRelevancia(){
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
