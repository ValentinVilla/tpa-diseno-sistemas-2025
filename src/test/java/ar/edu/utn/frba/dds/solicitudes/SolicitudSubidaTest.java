package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SolicitudSubidaTest {

  private HechoDinamico hecho;
  private SolicitudSubida solicitud;

  @BeforeEach
  void setUp() {
    hecho = new HechoDinamico(new HechoBuilder(), new Contribuyente(42, "juan", "perez"));
    solicitud = new SolicitudSubida(hecho, "motivo de subida", new ImplementadorSpam(10));
  }

  private HechoDinamico crearHecho(String titulo) {
    HechoBuilder hechoBase = new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(-34.65890546258081)
        .longitud(-58.467261290470084)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE);
    return new HechoDinamico(hechoBase, pablito = new Contribuyente(16, "Pablo", "Perez"));
  }

  //esta cheackeado que si hace lo que se le pide pero falla por ordenes de persistencia (preguntarle a tomi)
  @Test
  void puedeAceptarUltimaSolicitud() {
    HechoDinamico hechoDinamico = crearHecho("Pablito se robo un auto");
    // subir hecho (crea solicitud enestado PENDIENTE)
    entityManager.getTransaction().begin();
    entityManager.persist(pablito);
    entityManager.persist(fuenteDinamica);

    // Crea la soli
    fuenteDinamica.subirHecho(hechoDinamico);

    // agarrar la última solicitud
    RepositorioSolicitudes repo = RepositorioSolicitudes.getInstancia();
    ArrayList<Solicitud> solicitudes = new ArrayList<>(repo.obtenerTodas());
    Solicitud ultimaSolicitud = solicitudes.get(solicitudes.size() - 1);

    // simular aceptación
    ultimaSolicitud.aceptar();

    entityManager.flush();
    entityManager.getTransaction().commit();
    // verificar que el hecho quedó visible
    assertTrue(hechoDinamico.getVisible());
  }


}
