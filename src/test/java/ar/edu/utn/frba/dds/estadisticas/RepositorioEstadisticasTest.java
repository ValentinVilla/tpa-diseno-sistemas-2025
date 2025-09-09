package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioEstadisticas;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
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
        .visible(true)
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

    RepositorioEstadisticas repo = new RepositorioEstadisticas();

    String provinciaTop = repo.provinciaConMasHechos(coleccion);

    assertEquals("Córdoba", provinciaTop, "La provincia con más hechos debería ser Buenos Aires");
  }

  @Test
  void CategoriaConMasHechos() {
    RepositorioEstadisticas repo = new RepositorioEstadisticas();
    EstadisticaCategoriaTop provinciaTop = repo.obtenerCategoriaMasReportada();

    assertEquals("Testing con el berty", provinciaTop.getCategoria());
  }

  @Test
  void provinciaConMasHechosPorCategoria() {
    RepositorioEstadisticas repo = new RepositorioEstadisticas();

     EstadisticaProvinciaPorCategoriaTop provinciaTop = repo.obtenerProvinciaPorCategoria("Testing con el berty");
    assertEquals("Santa Fe", provinciaTop.getProvincia());
  }

  @Test
  void mayorHoraCategoria() {
    RepositorioEstadisticas repo = new RepositorioEstadisticas();
    EstadisticaHoraPorCategoriaTop horaTop =  repo.obtenerHoraPorCategoria("Testing con el berty");
    assertEquals(11, horaTop.getHora());
  }
}
