package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

import ar.edu.utn.frba.dds.servicios.GeorefAPI;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

public class RepositorioHechos {
    private final EntityManager entityManager;

    private static RepositorioHechos instancia;

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

  public void guardar(Hecho hecho) throws Exception {
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      if (hecho.getLatitud() != null && hecho.getLongitud() != null) {
        String provincia = GeorefAPI.getProvincia(
            hecho.getLatitud(), hecho.getLongitud()
        );
        System.out.println(provincia);
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

  public void eliminar(Long id) {
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      Coleccion coleccion = entityManager.find(Coleccion.class, id);
      if (coleccion != null) {
        entityManager.remove(coleccion);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
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
}

