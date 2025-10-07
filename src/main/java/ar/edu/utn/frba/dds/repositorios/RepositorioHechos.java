package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

import ar.edu.utn.frba.dds.servicios.GeorefAPI;
import ar.edu.utn.frba.dds.solicitudes.Solicitud;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

// CONVERSION DEL REPO HECHOS AL PATRON DAO
// EL REPOSITORIO TENDRIA QUE VOLAR, HAY QUE CORREGIR LOS TEST Y QUEDARNOS CON LAS FUNCIONALIDADES DEL FULLTEXTSEARCH
public class RepositorioHechos {
    private final EntityManager entityManager;

    private static RepositorioHechos instancia;

    //VUELA
    private RepositorioHechos() {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
      this.entityManager = emf.createEntityManager();
    }

    public static RepositorioHechos getInstancia() {
      if (instancia == null) {
        instancia = new RepositorioHechos();
      }
      return instancia;
    }


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



  // Para matchear a los hechos que cumplen las solicitudes
  public int actualizarVisibilidadPorTexto(String queryText, boolean visible) {
    Session session = entityManager.unwrap(Session.class);
    String sql = """
        UPDATE hecho
        SET visible = :visible
        WHERE fts_vector @@ plainto_tsquery('spanish', :queryText)
    """;
    NativeQuery<?> query = session.createNativeQuery(sql);
    query.setParameter("visible", visible);
    query.setParameter("queryText", queryText);
    return query.executeUpdate(); // devuelve la cantidad de filas modificadas
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


  private  EntityTransaction getEntity() {
    return entityManager.getTransaction();
  }
}

