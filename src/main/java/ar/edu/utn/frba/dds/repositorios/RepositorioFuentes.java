package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class RepositorioFuentes {
  private final EntityManager entityManager;
  private static RepositorioFuentes instancia;

  public RepositorioFuentes() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
    this.entityManager = emf.createEntityManager();
  }

  public static RepositorioFuentes getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioFuentes();
    }
    return instancia;
  }

  public List<Fuente> listarTodas() {
    return entityManager.createQuery("FROM Fuente", Fuente.class)
        .getResultList();
  }

  public List<FuenteDinamica> listarFuentesDinamicas() {
    String sql = "SELECT * FROM fuente WHERE tipo_fuente = 'FUENTE_DINAMICA'";
    return entityManager.createNativeQuery(sql, FuenteDinamica.class)
        .getResultList();
  }

  public FuenteDinamica buscarFuenteDinamicaPorId(Long id) {
    return entityManager.find(FuenteDinamica.class, id);
  }
}