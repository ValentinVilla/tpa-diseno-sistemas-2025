package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaCategoriaTop;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaHoraPorCategoriaTop;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaProvinciaPorCategoriaTop;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaProvinciaPorColeccion;
import ar.edu.utn.frba.dds.estadisticas.EstadisticaSolicitudesSpam;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.*;

public class RepositorioEstadisticas {

  private static RepositorioEstadisticas instancia;

  private RepositorioEstadisticas() {}

  public static RepositorioEstadisticas getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioEstadisticas();
    }
    return instancia;
  }

  //consulta para saber cual es la provincia con mas hechos segun una coleccion
  public String provinciaConMasHechosSegunColeccion(Coleccion coleccion){
    EntityManager em = this.getEntityManager();

    String sql = "SELECT provincia FROM estadistica_provincia_por_coleccion WHERE coleccion_id = ?";
    String provincia = (String) em.createNativeQuery(sql)
        .setParameter(1, coleccion.getId())
        .getSingleResult();

    if (provincia.isEmpty()) {
      return null;
    }

    return provincia;

  }

  //metodo que actualiza o llena la tabla estadistica_provincia_por_coleccion para todas las colecciones
  public void cargarTablaProvinciaConMasHechosXColeccion(){
    RepositorioColecciones repositorioColecciones = RepositorioColecciones.getInstancia();
    List<Coleccion> todasLasColecciones = repositorioColecciones.listarTodas();
    EntityManager em = this.getEntityManager();
    todasLasColecciones.forEach(coleccion ->cargarFilaStatProvinciaXColeccion(coleccion));
  }

  //metodo que carga SOLO una fila en la tabla estadistica_provincia_por_coleccion
  public void cargarFilaStatProvinciaXColeccion(Coleccion coleccion){
    List<Hecho> hechos = coleccion.mostrarHechos();
    EntityManager em = this.getEntityManager();

    em.createNativeQuery("CREATE TEMP TABLE hechos_temp (hecho_id BIGINT)").executeUpdate();

    hechos.stream().map( hecho -> hecho.getId()).toList().forEach( id ->
        em.createNativeQuery("INSERT INTO hechos_temp (hecho_id) VALUES (?)")
            .setParameter(1, id)
            .executeUpdate()
    );

    String insertSql =
        "INSERT INTO estadistica_provincia_por_coleccion (provincia, coleccion_id, cantidad) " +
            "SELECT h.provincia, :coleccionId, COUNT(*) " +
            "FROM HECHO h " +
            "WHERE h.id IN (SELECT hecho_id FROM hechos_temp) " +
            "GROUP BY h.provincia " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT 1";

    em.createNativeQuery(insertSql)
        .setParameter("coleccionId", coleccion.getId())
        .executeUpdate();

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

  public EstadisticaSolicitudesSpam obtenerCantidadSpam() {
    EntityManager em = this.getEntityManager();
    List<EstadisticaSolicitudesSpam> resultados = em.createQuery
        ("FROM EstadisticaSolicitudesSpam", EstadisticaSolicitudesSpam.class)
        .getResultList();

    if (resultados.isEmpty()) {
      return null;
    }

    return resultados.get(0);
  }

  @Transactional
  public void refrescarTodas() {
    EntityManager em = getEntityManager();
    em.getTransaction().begin();

    cargarTablaProvinciaConMasHechosXColeccion();//este metodo me actualiza la tabla estadistica_provincia_por_coleccion
    em.createNativeQuery("REFRESH MATERIALIZED VIEW estadistica_categoria_top").executeUpdate();
    em.createNativeQuery("REFRESH MATERIALIZED VIEW estadistica_hora_top_por_categoria").executeUpdate();
    em.createNativeQuery("REFRESH MATERIALIZED VIEW estadistica_provincia_por_categoria_top").executeUpdate();
    em.createNativeQuery("REFRESH MATERIALIZED VIEW cantidad_de_solicitudes_spam").executeUpdate();

    em.getTransaction().commit();
    }

  private EntityManager getEntityManager() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    return emf.createEntityManager();
  }
}
