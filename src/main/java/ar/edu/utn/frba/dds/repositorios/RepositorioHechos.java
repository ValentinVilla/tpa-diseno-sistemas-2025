package ar.edu.utn.frba.dds.repositorios;

    import ar.edu.utn.frba.dds.dominio.Coleccion;
    import ar.edu.utn.frba.dds.dominio.Hecho;

    import javax.persistence.EntityManager;
    import javax.persistence.EntityTransaction;
    import java.util.List;

    import org.hibernate.Session;
    import org.hibernate.query.NativeQuery;
    import java.util.List;
    import java.util.Arrays;
    import java.util.stream.Collectors;

    public class RepositorioHechos {
      private final EntityManager entityManager;

      public RepositorioHechos(EntityManager entityManager) {
        this.entityManager = entityManager;
      }

      public void guardar(Hecho hecho) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
          transaction.begin();
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
        String sql = "SELECT *, ts_rank(fts_vector, plainto_tsquery('spanish', :queryText)) AS rank " + // cambie a plainto_tsquery porque esto ya agrega los AND entre palabras, (lo que hacias en el hecho service)
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
            "WHERE similarity(titulo, :queryText) > 0.1 " +   // umbral configurable, cadena identica 1.0, nada  0.0
            "ORDER BY sim DESC";

        NativeQuery<Hecho> query = session.createNativeQuery(sql, Hecho.class);
        query.setParameter("queryText", queryText);
        return query.getResultList();
      }
    }

