package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.solicitudes.EstadoSolicitud;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

// CONVERSION DEL REPO HECHOS AL PATRON DAO
// EL REPOSITORIO TENDRIA QUE VOLAR, HAY QUE CORREGIR LOS TEST Y QUEDARNOS CON LAS FUNCIONALIDADES DEL FULLTEXTSEARCH
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

/*
    //VUELA
    public void guardar(Hecho hecho) throws Exception {
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      if (hecho.getLatitud() != null && hecho.getLongitud() != null) {
        String provincia = GeorefAPI.getProvincia(
            hecho.getLatitud(), hecho.getLongitud()
        );
        hecho.setProvincia(provincia);
      }

      entityManager.persist(hecho);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  //VUELA
  public void actualizar(Hecho hecho) {
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      entityManager.merge(hecho);
      entityManager.flush();
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  //VUELA
  public void eliminar(Long id) {
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      Hecho hecho = entityManager.find(Hecho.class, id);
      if (hecho != null) {
        entityManager.remove(hecho);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }
*/


    // Para matchear a los hechos que cumplen las solicitudes
    public int actualizarVisibilidadPorTexto(String hechoBuscado, boolean visible) {
      Session session = entityManager.unwrap(Session.class);
      String sql = """
          UPDATE hechoDinamico
          SET visible = :visible
          WHERE fts_vector @@ plainto_tsquery('spanish', :queryText)
      """;
      NativeQuery<?> query = session.createNativeQuery(sql);
      query.setParameter("visible", visible);
      query.setParameter("queryText", hechoBuscado);
      return query.executeUpdate(); // devuelve la cantidad de filas modificadas
    }

    public void actualizarHechoModificado(String hechoBuscado, String valoresActualizacion){
      String[] camposHecho = valoresActualizacion.split(";");
      LocalDate fechaModificacion = LocalDate.now();

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
          WHERE fts_vector @@ plainto_tsquery('spanish', :hechoBuscado)
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
      query.setParameter("queryText", hechoBuscado);
      query.executeUpdate();
    }

    public List<Hecho> buscarPorTextoEnDB(String queryText) {
      Session session = entityManager.unwrap(Session.class);
      String sql = "SELECT *, ts_rank(fts_vector, plainto_tsquery('spanish', :queryText)) AS rank " +
          "FROM hecho " +
          "WHERE fts_vector @@ plainto_tsquery('spanish', :queryText) " +
          "ORDER BY rank DESC";
      NativeQuery<Hecho> query = session.createNativeQuery(sql, Hecho.class);
      query.setParameter("queryText", queryText);
      return query.getResultList();
    }

    public List<Hecho> buscarPorSimilitud(String queryText) {
      Session session = entityManager.unwrap(Session.class);

      String sql = "SELECT *, similarity(titulo, :queryText) AS sim " +
          "FROM hecho " +
          "WHERE similarity(titulo, :queryText) > 0.1 " +
          "ORDER BY sim DESC";

      NativeQuery<Hecho> query = session.createNativeQuery(sql, Hecho.class);
      query.setParameter("queryText", queryText);
      return query.getResultList();
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

    public ArrayList<HechoDinamico> losQueNoFueronEliminadosDinamicos(ArrayList<HechoDinamico> hechos) {
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


    private  EntityTransaction getEntity() {
    return entityManager.getTransaction();
  }

}

