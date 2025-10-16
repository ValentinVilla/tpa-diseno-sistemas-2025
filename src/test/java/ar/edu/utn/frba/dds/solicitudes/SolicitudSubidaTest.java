package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.model.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudSubida;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import org.junit.jupiter.api.BeforeEach;

public class SolicitudSubidaTest {

  private HechoDinamico hecho;
  private SolicitudSubida solicitud;

  @BeforeEach
  void setUp() {
    hecho = new HechoDinamico(new HechoBuilder(), new Contribuyente("tobi", "juan", 1127587650, "tobi@gmail.com", 21, "micontrasenia"));
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
