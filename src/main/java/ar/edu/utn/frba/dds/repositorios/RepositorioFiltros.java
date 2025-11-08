package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.helpers.EntityManagerFactoryProvider;
import ar.edu.utn.frba.dds.model.filtros.Filtro;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class RepositorioFiltros {
  private final EntityManager entityManager;
  private static RepositorioFiltros instancia;

  public RepositorioFiltros() {
    EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
    this.entityManager = emf.createEntityManager();
  }

  public static RepositorioFiltros getInstancia() {
    if (instancia == null) instancia = new RepositorioFiltros();
    return instancia;
  }

  public List<Filtro> listarTodas() {
    return entityManager.createQuery("FROM Filtro", Filtro.class).getResultList();
  }

  public Filtro buscarPorID(Long id) {
    return entityManager.find(Filtro.class, id);
  }

  public void guardar(Filtro filtro) {
    try {
      entityManager.getTransaction().begin();
      entityManager.persist(filtro);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
      throw e;
    }
  }

  public void actualizar(Filtro filtro) {
    try {
      entityManager.getTransaction().begin();
      entityManager.merge(filtro);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
      throw e;
    }
  }
}

