package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.model.solicitudes.EstadoSolicitud;
import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class RepositorioSolicitudes {
  private final EntityManager entityManager;

  private static RepositorioSolicitudes instancia;

  private RepositorioSolicitudes() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    this.entityManager = emf.createEntityManager();
  }

  public static RepositorioSolicitudes getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioSolicitudes();
    }
    return instancia;
  }

  public List<Solicitud> obtenerTodas() {
    return entityManager.createQuery("SELECT s FROM Solicitud s ORDER BY fecha ASC NULLS LAST", Solicitud.class)
        .getResultList();
  }

  public List<Solicitud> obtenerTodasPendientes() {
    return entityManager.createQuery("SELECT s FROM Solicitud s WHERE s.estado = :estado ORDER BY fecha ASC NULLS LAST", Solicitud.class)
        .setParameter("estado", EstadoSolicitud.PENDIENTE)
        .getResultList();
  }

  public void guardar(Solicitud solicitud) {
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      entityManager.persist(solicitud);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  public void eliminar(Long id) {
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      Solicitud solicitud = entityManager.find(Solicitud.class, id);
      if (solicitud != null) {
        entityManager.remove(solicitud);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  public void actualizar(Solicitud solicitud) {
    EntityTransaction transaction = getEntity();
    try {
      transaction.begin();
      entityManager.merge(solicitud);
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  private  EntityTransaction getEntity() {
      return entityManager.getTransaction();
  }
}