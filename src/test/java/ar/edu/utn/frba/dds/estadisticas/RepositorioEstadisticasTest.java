package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioEstadisticas;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class RepositorioEstadisticasTest {

  private EntityManagerFactory emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
  private EntityManager entityManager = emf.createEntityManager();
  /*
  private Hecho crearHecho(double latitud, double longitud) {
    return new HechoBuilder()
        .titulo("Prueba persistance")
        .descripcion("desc")
        .categoria("cat")
        .latitud(latitud)
        .longitud(longitud)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE)
        .build();
  }

  private HechoDinamico crearHechoDinamico(String titulo) {
    HechoBuilder hechoBase = new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(-31.4167)
        .longitud(-64.1833)
        .fechaAcontecimiento(LocalDateTime.now())
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE);
    return new HechoDinamico(hechoBase, new Contribuyente(42, "Juan", "Plomero"));
  }

  @Test
  void testProvinciaConMasHechos() throws Exception {
    Hecho h1 = crearHecho(-31.4167, -64.1833);
    Hecho h2 = crearHecho(-34.6037, -58.3816);
    Hecho h3 = crearHecho(-31.4167, -64.1833);
    Hecho h4 = crearHecho(-31.6333, -60.7000);

    DAOHechos repoHechos = DAOHechos.getInstancia();

    Coleccion coleccion = new Coleccion() {
      @Override
      public List<Hecho> mostrarHechos() {
        return Arrays.asList(h1, h2, h3, h4);
      }
    };

    RepositorioEstadisticas repo = RepositorioEstadisticas.getInstancia();

    String provinciaTop = repo.provinciaConMasHechos(coleccion);

    assertEquals("Cordoba", provinciaTop, "La provincia con más hechos debería ser Buenos Aires");
  }

  @Test
  void CategoriaConMasHechos() {
    RepositorioEstadisticas repo = RepositorioEstadisticas.getInstancia();
    EstadisticaCategoriaTop provinciaTop = repo.obtenerCategoriaMasReportada();

    assertEquals("Testing con el berty", provinciaTop.getCategoria());
  }

  @Test
  void provinciaConMasHechosPorCategoria() {
    RepositorioEstadisticas repo = RepositorioEstadisticas.getInstancia();

     EstadisticaProvinciaPorCategoriaTop provinciaTop = repo.obtenerProvinciaPorCategoria("Testing con el berty");
    assertEquals("Santa Fe", provinciaTop.getProvincia());
  }

  @Test
  void mayorHoraCategoria() {
    RepositorioEstadisticas repo = RepositorioEstadisticas.getInstancia();
    EstadisticaHoraPorCategoriaTop horaTop =  repo.obtenerHoraPorCategoria("Testing con el berty");
    assertEquals(11, horaTop.getHora());
  }

  @Test
  public void porcetajeDeSpamEsDel50() throws Exception {
    HechoDinamico hecho = crearHechoDinamico("hecho dinamico de prueba");

    DetectorDeSpam detectorSiempreTrue = texto -> true;
    SolicitudEliminacion s1 = new SolicitudEliminacion(hecho, "spam", detectorSiempreTrue);
    SolicitudEliminacion s2 = new SolicitudEliminacion(hecho, "spam", detectorSiempreTrue);

    DetectorDeSpam detectorSiempreFalse = texto -> false;
    SolicitudEliminacion s3 = new SolicitudEliminacion(hecho, "spam", detectorSiempreFalse);
    SolicitudEliminacion s4 = new SolicitudEliminacion(hecho, "spam", detectorSiempreFalse);

    SolicitudModificacion sModificacion = new SolicitudModificacion(hecho, "no se cuenta en la estadistica",detectorSiempreTrue, hecho);

    RepositorioSolicitudes repo = RepositorioSolicitudes.getInstancia();

    repo.guardar(s1);
    repo.guardar(s2);
    repo.guardar(s3);
    repo.guardar(s4);
    repo.guardar(sModificacion);

    RepositorioEstadisticas repoEstadisticas = RepositorioEstadisticas.getInstancia();
    EstadisticaSolicitudesSpam estadisticasSpam = repoEstadisticas.obtenerCantidadSpam();
    assertEquals(4, estadisticasSpam.getCantidad());
    assertEquals("50.00%", estadisticasSpam.getPorcentaje());
  }
  */
}
