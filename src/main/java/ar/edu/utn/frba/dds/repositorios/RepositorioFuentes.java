package ar.edu.utn.frba.dds.repositorios;
import ar.edu.utn.frba.dds.model.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudSubida;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RepositorioFuentes {

  private final EntityManagerFactory emf;
  private static RepositorioFuentes instancia;

  private RepositorioFuentes() {
    this.emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
  }

  public static synchronized RepositorioFuentes getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioFuentes();
    }
    return instancia;
  }

  private <R> R withEntityManager(Function<EntityManager, R> work) {
    EntityManager em = emf.createEntityManager();
    try {
      return work.apply(em);
    } finally {
      if (em.isOpen()) em.close();
    }
  }

  private void inTransaction(Consumer<EntityManager> work) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      work.accept(em);
      tx.commit();
    } catch (RuntimeException e) {
      if (tx.isActive()) tx.rollback();
      throw e;
    } finally {
      if (em.isOpen()) em.close();
    }
  }

  public List<Fuente> listarTodas() {
    return withEntityManager(em ->
        em.createQuery("SELECT f FROM Fuente f", Fuente.class)
            .getResultList()
    );
  }

  public List<FuenteDinamica> listarFuentesDinamicas() {
    return withEntityManager(em ->
        em.createQuery("SELECT f FROM FuenteDinamica f", FuenteDinamica.class)
            .getResultList()
    );
  }

  public FuenteDinamica buscarFuenteDinamicaPorId(Long id) {
    return withEntityManager(em -> em.find(FuenteDinamica.class, id));
  }

  public void agregarHechoALaFuente(Long fuenteId, HechoDinamico hecho) { //todo: ponete a laburar berty
    inTransaction(em -> {
      FuenteDinamica fuente = em.find(FuenteDinamica.class, fuenteId);
      if (fuente == null) {
        throw new IllegalArgumentException("Fuente no encontrada id=" + fuenteId);
      }
      hecho.revisarubicacion();

      fuente.subirHecho(hecho);

      SolicitudSubida solicitud = new SolicitudSubida(hecho, "", new ImplementadorSpam(10), null);
      em.persist(solicitud);
    });
  }

  public Fuente buscarPorID(Long id) {
    return emf.createEntityManager().find(Fuente.class, id);
  }

}