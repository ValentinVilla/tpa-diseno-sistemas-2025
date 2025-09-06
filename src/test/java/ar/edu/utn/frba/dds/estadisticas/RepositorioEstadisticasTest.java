package ar.edu.utn.frba.dds.estadisticas;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioEstadisticas;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositorioEstadisticasTest {
  private HechoBuilder crearHecho(String provincia) {
    return new HechoBuilder()
        .titulo("Prueba persistance")
        .descripcion("desc")
        .categoria("cat")
        .latitud(-34.6037)
        .longitud(-58.3816)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE)
        .provincia(provincia);
  }

  @Test
  void testProvinciaConMasHechos() throws Exception {
    // Creamos hechos de ejemplo
    Hecho h1 = new  Hecho(crearHecho("Buenos Aires"));
    Hecho h2 = new Hecho(crearHecho("Buenos Aires"));
    Hecho h3 = new Hecho(crearHecho("Cordoba"));
    Hecho h4 = new Hecho(crearHecho("Santa fe"));  // provincia desconocida

    // Creamos una colección mockeando mostrarHechos()
    Coleccion coleccion = new Coleccion() {
      @Override
      public List<Hecho> mostrarHechos() {
        return Arrays.asList(h1, h2, h3, h4);
      }
    };

    RepositorioEstadisticas repo = new RepositorioEstadisticas();

    String provinciaTop = repo.provinciaConMasHechos(coleccion);

    assertEquals("Buenos Aires", provinciaTop, "La provincia con más hechos debería ser Buenos Aires");
  }
}
