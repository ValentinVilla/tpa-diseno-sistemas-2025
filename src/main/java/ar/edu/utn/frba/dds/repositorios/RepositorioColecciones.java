package ar.edu.utn.frba.dds.repositorios;

import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frba.dds.dominio.Coleccion;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioColecciones {
  private final EntityManager entityManager;

  public RepositorioColecciones(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void guardar(Coleccion coleccion) {
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      entityManager.persist(coleccion); // esto seriaun INSERT
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  public Coleccion buscarPorID(Long id) {
    return entityManager.find(Coleccion.class, id); // esto seria un SELECT con WHERE id = ?
  }

  public List<Coleccion> listarTodas() {
    return entityManager.createQuery("SELECT c FROM Coleccion c", Coleccion.class)
        .getResultList(); // esto devuelve objetos de tipo Coleccion, no se hace select * porque
                          // es JPQL, no SQL
  }//ESTO PARA MI HAY QUE TESTEARLO

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

  public void actualizar(Coleccion coleccion) {
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      entityManager.merge(coleccion); // merge es como un UPDATE
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }
}
