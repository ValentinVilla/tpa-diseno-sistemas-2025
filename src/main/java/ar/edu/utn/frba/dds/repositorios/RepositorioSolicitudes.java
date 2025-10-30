package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class RepositorioSolicitudes {
  private final EntityManagerFactory emf;

  private static RepositorioSolicitudes instancia;

  private RepositorioSolicitudes() {
    this.emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
  }

  public static RepositorioSolicitudes getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioSolicitudes();
    }
    return instancia;
  }

  public List<Solicitud> obtenerTodas() {
    EntityManager em = emf.createEntityManager();
    try {
      return em.createQuery("SELECT s FROM Solicitud s", Solicitud.class)
          .getResultList();
    } finally {
      em.close();
    }
  }

  public void guardar(Solicitud solicitud) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    try {
      transaction.begin();
      em.persist(solicitud);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    } finally {
      em.close();
    }
  }

  public void eliminar(Long id) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    try {
      transaction.begin();
      Solicitud solicitud = em.find(Solicitud.class, id);
      if (solicitud != null) {
        em.remove(solicitud);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    } finally {
      em.close();
    }
  }

  public void actualizar(Solicitud solicitud) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    try {
      transaction.begin();
      em.merge(solicitud);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null && transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    } finally {
      em.close();
    }
  }
}