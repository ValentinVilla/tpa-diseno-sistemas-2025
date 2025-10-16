package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class RepositorioUsuarios {

    private final EntityManager entityManager;

    private static RepositorioUsuarios instancia;

    private RepositorioUsuarios() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
        this.entityManager = emf.createEntityManager();
    }

    public static RepositorioUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioUsuarios();
        }
        return instancia;
    }

    public void guardar(Contribuyente contribuyente) {
        EntityTransaction transaction = getEntity();
        try {
            transaction.begin();
            entityManager.persist(contribuyente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Contribuyente buscarUsuario(String nombre, String contrasenia){
        try {
            return entityManager.createQuery(
                    "SELECT c FROM Contribuyente c WHERE c.nombre = :nombre AND c.password = :contrasenia", Contribuyente.class)
                .setParameter("nombre", nombre)
                .setParameter("contrasenia", contrasenia)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    private  EntityTransaction getEntity() {
        return entityManager.getTransaction();
    }


}//TODO: CONVIENE QUE ABSTRAIGAMOS LA LOGICA DE LOS REPOSITORIOS EN UNA CLASE PADRE TODOS HACEN LO MISMO y que trabajen con un objeto de tipo Entity !!!
