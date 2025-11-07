package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.helpers.EntityManagerFactoryProvider;
import ar.edu.utn.frba.dds.model.dominio.Media;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class RepositorioMedia {
  private final EntityManager entityManager;
  private static RepositorioMedia instancia;

  public RepositorioMedia() {
    EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
    this.entityManager = emf.createEntityManager();
  }

  public static RepositorioMedia getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioMedia();
    }
    return instancia;
  }

  public List<Media> findByHechoId(Long hechoId) {
    TypedQuery<Media> q = entityManager.createQuery(
        "SELECT m FROM Media m WHERE m.hecho.id = :hechoId ORDER BY m.id ASC",
        Media.class);
    q.setParameter("hechoId", hechoId);
    return q.getResultList();
  }
}
