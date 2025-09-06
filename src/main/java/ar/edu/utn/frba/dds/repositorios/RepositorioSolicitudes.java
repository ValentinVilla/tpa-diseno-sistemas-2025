package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.solicitudes.Solicitud;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudes {
  private final EntityManager entityManager;

  private final List<Solicitud> solicitudes = new ArrayList<>();

    public RepositorioSolicitudes(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Solicitud> obtenerTodas() {
      return entityManager.createQuery("SELECT s FROM Solicitud s", Solicitud.class)
          .getResultList();
  }

  public void guardar(Solicitud solicitud) {
    EntityTransaction transaction = entityManager.getTransaction();
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
    EntityTransaction transaction = entityManager.getTransaction();
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
    EntityTransaction transaction = entityManager.getTransaction();
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


}