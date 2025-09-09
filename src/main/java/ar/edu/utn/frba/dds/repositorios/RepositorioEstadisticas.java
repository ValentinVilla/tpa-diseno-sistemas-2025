package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaCategoriaTop;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaHoraPorCategoriaTop;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaProvinciaPorCategoriaTop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.*;

public class RepositorioEstadisticas {
  @Transactional
  public void refrescarCategoriaTop() {
    EntityManager em = this.getEntityManager();
    em.createNativeQuery("REFRESH MATERIALIZED VIEW estadistica_categoria_top").executeUpdate();
  }

  @Transactional
  public void refrescarProvinciaPorCategoriaTop() {
    EntityManager em = this.getEntityManager();
    em.createNativeQuery("REFRESH MATERIALIZED VIEW estadistica_provincia_por_categoria_top").executeUpdate();
  }

  @Transactional
  public void refrescarHoraPorCategoriaTop() {
    EntityManager em = this.getEntityManager();
    em.createNativeQuery("REFRESH MATERIALIZED VIEW estadistica_hora_top_por_categoria").executeUpdate();
  }

  public String provinciaConMasHechos(Coleccion coleccion) {
    List<Hecho> hechos = coleccion.mostrarHechos();

    Map<String, Long> conteoPorProvincia = hechos.stream()
        .filter(h -> h.getProvincia() != null)
        .collect(Collectors.groupingBy(
            Hecho::getProvincia,
            Collectors.counting()
        ));

    return conteoPorProvincia.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Sin datos");
  }

  public EstadisticaCategoriaTop obtenerCategoriaMasReportada() {
    EntityManager em = this.getEntityManager();
    List<EstadisticaCategoriaTop> resultados = em.createQuery(
      "FROM EstadisticaCategoriaTop", EstadisticaCategoriaTop.class)
        .getResultList();

    if (resultados.isEmpty()) {
      return null;
    }

    return resultados.get(0);
  }


  public EstadisticaProvinciaPorCategoriaTop obtenerProvinciaPorCategoria(String categoria) {
    EntityManager em = this.getEntityManager();
    List<EstadisticaProvinciaPorCategoriaTop> resultados = em.createQuery("FROM EstadisticaProvinciaPorCategoriaTop e WHERE e.categoria = :cat", EstadisticaProvinciaPorCategoriaTop.class)
        .setParameter("cat", categoria)
        .getResultList();
    if (resultados.isEmpty()) {
      return null;
    }
    return resultados.get(0);
  }

  public EstadisticaHoraPorCategoriaTop obtenerHoraPorCategoria(String categoria) {
    EntityManager em = this.getEntityManager();

    List<EstadisticaHoraPorCategoriaTop> resultados = em.createQuery("FROM EstadisticaHoraPorCategoriaTop e WHERE e.categoria = :cat", EstadisticaHoraPorCategoriaTop.class)
        .setParameter("cat", categoria)
        .getResultList();
    if (resultados.isEmpty()) {
      return null;
    }
    return resultados.get(0);
  }

  private EntityManager getEntityManager() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    return emf.createEntityManager();
  }
}
