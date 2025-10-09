package ar.edu.utn.frba.dds.repositorios;

import java.util.List;
import ar.edu.utn.frba.dds.model.dominio.Coleccion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class RepositorioColecciones {
  private final EntityManager entityManager;
  private static RepositorioColecciones instancia;

  public RepositorioColecciones() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    this.entityManager = emf.createEntityManager();
  }

  public static RepositorioColecciones getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioColecciones();
    }
    return instancia;
  }

  public void guardar(Coleccion coleccion) {
    EntityTransaction transaction = getTransaction();
    try {
      transaction.begin();
      entityManager.persist(coleccion);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  public Coleccion buscarPorID(Long id) {
    return entityManager.find(Coleccion.class, id);
  }

  public List<Coleccion> listarTodas() {
    return entityManager.createQuery("SELECT c FROM Coleccion c", Coleccion.class)
        .getResultList();
  }

  public void eliminar(Long id) {
    EntityTransaction transaction = getTransaction();
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

  public void actualizar(Coleccion coleccion) {
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      entityManager.merge(coleccion);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  private EntityTransaction getTransaction() {
    return entityManager.getTransaction();
  }
}
