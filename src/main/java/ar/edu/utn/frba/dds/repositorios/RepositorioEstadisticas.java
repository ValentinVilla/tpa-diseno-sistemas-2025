package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.*;

public class RepositorioEstadisticas {
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

  public String obtenerCategoriaMasReportada() {
    String jpql = "SELECT h.categoria, COUNT(h) " +
        "FROM Hecho h " +
        "GROUP BY h.categoria " +
        "ORDER BY COUNT(h) DESC";

    EntityManager em = this.getEntityManager();
    TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
    query.setMaxResults(1);

    Object[] resultado = query.getSingleResult();

    return (String) resultado[0];
  }

  public String obtenerProvinciaConMasHechosPorCategoria(String categoria) {
    String jpql = "SELECT h.provincia, COUNT(h) " +
        "FROM Hecho h " +
        "WHERE h.categoria = :categoria " +
        "GROUP BY h.provincia " +
        "ORDER BY COUNT(h) DESC";

    EntityManager em = this.getEntityManager();
    TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
    query.setParameter("categoria", categoria);
    query.setMaxResults(1);

    Object[] resultado = query.getSingleResult();

    return (String) resultado[0];
  }

  private EntityManager getEntityManager() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    return emf.createEntityManager();
  }
}
