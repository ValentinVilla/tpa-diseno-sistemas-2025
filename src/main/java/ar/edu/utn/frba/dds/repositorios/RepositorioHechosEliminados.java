package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.HechoEliminado;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RepositorioHechosEliminados {
    private static RepositorioHechosEliminados instancia;
    private final EntityManager entityManager;


    public void agregarHechoEliminado(HechoEliminado hecho) {
        entityManager.getTransaction().begin();
        entityManager.persist(hecho);
        entityManager.getTransaction().commit();
    }

    public boolean fueEliminado(Hecho hecho) {
        List<HechoEliminado> hechosEliminados = entityManager.createQuery("SELECT h FROM HechoEliminado h", HechoEliminado.class)
            .getResultList();
        return hechosEliminados.stream().anyMatch(hechoEliminado -> hechoEliminado.correspondeA(hecho));
    }


    public ArrayList<Hecho> losQueNoFueronEliminados(ArrayList<Hecho> hechos) {
        List<HechoEliminado> hechosEliminados = entityManager.createQuery("SELECT h FROM HechoEliminado h", HechoEliminado.class)
            .getResultList();
        return hechos.stream()
            .filter(hecho -> hechosEliminados.stream()
                .noneMatch(hechoEliminado -> hechoEliminado.correspondeA(hecho)))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<HechoDinamico> losQueNoFueronEliminadosDinamicos(ArrayList<HechoDinamico> hechos) {
        List<HechoEliminado> hechosEliminados = entityManager.createQuery("SELECT h FROM HechoEliminado h", HechoEliminado.class)
            .getResultList();
        return hechos.stream()
            .filter(hecho -> hechosEliminados.stream()
                .noneMatch(hechoEliminado -> hechoEliminado.correspondeA(hecho)))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public static RepositorioHechosEliminados getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioHechosEliminados();
        }
        return instancia;
    }

    private RepositorioHechosEliminados() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
        this.entityManager = emf.createEntityManager();
    }
}//TODO: QUE TODAS LAS FUENTES ANTES DE CARGAR HECHOS FILTREN POR LOS QUE NO ESTAN ELIMINADOS (USAR EL REPO DE HECHOS ELIMINADOS)
