package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.model.dominio.Hecho;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.solicitudes.EstadoSolicitud;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

public class DAOHechos {
  private final EntityManager entityManager;

  private static DAOHechos instancia;

  private DAOHechos() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    this.entityManager = emf.createEntityManager();
  }

  public static DAOHechos getInstancia() {
    if (instancia == null) {
      instancia = new DAOHechos();
    }
    return instancia;
  }

  // Para matchear a los hechos que cumplen las solicitudes
  public void actualizarVisibilidadPorTexto(String titulo, String descripcion, String categoria, boolean visible) {
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      Session session = entityManager.unwrap(Session.class);
      String sql = """
          UPDATE hechoDinamico
          SET visible = :visible
          WHERE similarity(titulo, :titulo) > 0.1 
          AND similarity(categoria, :categoria) > 0.1 
          AND descripcion = :descripcion
      """;
      NativeQuery<?> query = session.createNativeQuery(sql);
      query.setParameter("visible", visible);
      query.setParameter("titulo", titulo);
      query.setParameter("categoria", categoria);
      query.setParameter("descripcion", descripcion);
      query.executeUpdate();
      transaction.commit();
    } catch(Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  public void actualizarHechoModificado(String titulo, String descripcion, String categoria, String valoresActualizacion){
    String[] camposHecho = valoresActualizacion.split(";");
    LocalDate fechaModificacion = LocalDate.now();
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      Session session = entityManager.unwrap(Session.class);
      String sql = """
            UPDATE hechoDinamico
            SET titulo = :camposHecho[0],
                descripcion = :camposHecho[1],
                categoria = :camposHecho[2],
                latitud = :camposHecho[3],
                longitud = :camposHecho[4],
                fechaAcontecimiento= :camposHecho[5],
                provincia = :camposHecho[6],
                fechaModificacion = :fechaModiciacion
            WHERE similarity(titulo, :titulo) > 0.1 
            AND similarity(categoria, :categoria) > 0.1 
            AND descripcion = :descripcion 
        """;
      NativeQuery<?> query = session.createNativeQuery(sql);
      query.setParameter("titulo", camposHecho[0]);
      query.setParameter("descripcion", camposHecho[1]);
      query.setParameter("categoria", camposHecho[2]);
      query.setParameter("latitud", camposHecho[3]);
      query.setParameter("longitud", camposHecho[4]);
      query.setParameter("fechaAcontecimiento", camposHecho[5]);
      query.setParameter("provincia", camposHecho[6]);
      query.setParameter("fechaModificacion", fechaModificacion);
      query.setParameter("titulo", titulo);
      query.setParameter("categoria", categoria);
      query.setParameter("descripcion", descripcion);
      query.executeUpdate();
      transaction.commit();
    } catch(Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  public List<Hecho> buscarPorTextoEnDB(String queryText) {
    Session session = entityManager.unwrap(Session.class);
    String sql = "SELECT *, ts_rank(fts_vector, plainto_tsquery('spanish', :queryText)) AS rank " +
        "FROM hechoDinamico " +
        "WHERE fts_vector @@ plainto_tsquery('spanish', :queryText) " +
        "ORDER BY rank DESC";
    NativeQuery<HechoDinamico> query = session.createNativeQuery(sql, HechoDinamico.class);
    query.setParameter("queryText", queryText);

    return new ArrayList<>(query.getResultList());
  }

  public List<Hecho> buscarPorSimilitud(String queryText) {
    Session session = entityManager.unwrap(Session.class);

    String sql = "SELECT *, similarity(titulo, :queryText) AS sim " +
        "FROM hechoDinamico " +
        "WHERE similarity(titulo, :queryText) > 0.1 " +
        "ORDER BY sim DESC";

    NativeQuery<HechoDinamico> query = session.createNativeQuery(sql, HechoDinamico.class);
    query.setParameter("queryText", queryText);
    return new ArrayList<>(query.getResultList());
  }

  public List<Hecho> buscarPorTextoPorFuente(String queryText, Long fuenteId) {
    Session session = entityManager.unwrap(Session.class);
    String sql = """
        SELECT *, ts_rank(fts_vector, plainto_tsquery('spanish', :queryText)) AS rank
        FROM hechoDinamico
        WHERE fts_vector @@ plainto_tsquery('spanish', :queryText)
          AND fuente_id = :fuenteId
        ORDER BY rank DESC
        """;

    NativeQuery<HechoDinamico> query = session.createNativeQuery(sql, HechoDinamico.class);
    query.setParameter("queryText", queryText);
    query.setParameter("fuenteId", fuenteId);

    return new ArrayList<>(query.getResultList());
  }


  public List<Hecho> buscarPorSimilitudPorFuente(String queryText, Long fuenteId) {
    Session session = entityManager.unwrap(Session.class);
    String sql = """
        SELECT *, similarity(titulo, :queryText) AS sim
        FROM hechoDinamico
        WHERE similarity(titulo, :queryText) > 0.1
          AND fuente_id = :fuenteId
        ORDER BY sim DESC
        """;

    NativeQuery<HechoDinamico> query = session.createNativeQuery(sql, HechoDinamico.class);
    query.setParameter("queryText", queryText);
    query.setParameter("fuenteId", fuenteId);

    return new ArrayList<>(query.getResultList());
  }


  //------------------- GESTOR LAS ELIMINACIONES APROBADAS --------------------//

  public boolean fueEliminado(Hecho hecho) {
    List<String> valoresHechosEliminados = entityManager.createQuery(
            "SELECT s.valoresHecho FROM SolicitudEliminacion s WHERE s.estado = :estado", String.class)
        .setParameter("estado", EstadoSolicitud.ACEPTADA)
        .getResultList();
    String valorHecho = hecho.getTitulo() + " | " + hecho.getDescripcion() + " | " + hecho.getCategoria();
    return valoresHechosEliminados.contains(valorHecho);
  }

  public ArrayList<Hecho> losQueNoFueronEliminados(ArrayList<Hecho> hechos) {
    List<String> valoresHechosEliminados = entityManager.createQuery(
            "SELECT s.valoresHecho FROM SolicitudEliminacion s WHERE s.estado = :estado", String.class)
        .setParameter("estado", EstadoSolicitud.ACEPTADA)
        .getResultList();
    return hechos.stream()
        .filter(hecho -> {
          String valorHecho = hecho.getTitulo() + " | " + hecho.getDescripcion() + " | " + hecho.getCategoria();
          return !valoresHechosEliminados.contains(valorHecho);
        })
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private EntityTransaction getEntity() {
    return entityManager.getTransaction();
  }

}