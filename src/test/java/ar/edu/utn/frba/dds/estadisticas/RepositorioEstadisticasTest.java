package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.estadisticas.EstadisticaCategoriaTop;
import ar.edu.utn.frba.dds.model.estadisticas.EstadisticaHoraPorCategoriaTop;
import ar.edu.utn.frba.dds.model.estadisticas.EstadisticaProvinciaPorCategoriaTop;
import ar.edu.utn.frba.dds.model.estadisticas.EstadisticaSolicitudesSpam;
import ar.edu.utn.frba.dds.repositorios.RepositorioEstadisticas;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudModificacion;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositorioEstadisticasTest {
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

  @Test
  void testProvinciaConMasHechos() throws Exception {
    Hecho h1 = crearHecho(-31.4167, -64.1833);
    Hecho h2 = crearHecho(-34.6037, -58.3816);
    Hecho h3 = crearHecho(-31.4167, -64.1833);
    Hecho h4 = crearHecho(-31.6333, -60.7000);

    RepositorioHechos repoHechos = RepositorioHechos.getInstancia();

    repoHechos.guardar(h1);
    repoHechos.guardar(h2);
    repoHechos.guardar(h3);
    repoHechos.guardar(h4);

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
    Hecho hecho = crearHecho(-31.4167, -64.1833);

    RepositorioHechos repoHechos = RepositorioHechos.getInstancia();
    repoHechos.guardar(hecho);

    DetectorDeSpam detectorSiempreTrue = texto -> true;
    SolicitudEliminacion s1 = new SolicitudEliminacion("spam", hecho ,detectorSiempreTrue);
    SolicitudEliminacion s2 = new SolicitudEliminacion("spam", hecho ,detectorSiempreTrue);

    DetectorDeSpam detectorSiempreFalse = texto -> false;
    SolicitudEliminacion s3 = new SolicitudEliminacion("no es spam", hecho ,detectorSiempreFalse);
    SolicitudEliminacion s4 = new SolicitudEliminacion("no es spam", hecho ,detectorSiempreFalse);

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
}
