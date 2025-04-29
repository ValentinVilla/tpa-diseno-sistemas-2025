/*
package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.users.Administrador;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.*;
import ar.edu.utn.frba.dds.filtros.*;

public class ColeccionTest {
    @Test
    public void filtrarHechosPorCategoriaFuncionaCorrectamente() {
      // Arrange - Creo algunos hechos de prueba
      Hecho hecho1 = new Hecho("Incendio en Córdoba", "Incendio forestal grande", "Incendio forestal", -31.4167, -64.1833, LocalDate.of(2025, 1, 15), LocalDate.of(2025, 1, 16), Origen.DATASET);
      Hecho hecho2 = new Hecho("Contaminación en Riachuelo", "Alta polución", "Incendio forestal", -34.663, -58.368, LocalDate.of(2025, 2, 10), LocalDate.of(2025, 2, 11), Origen.DATASET);
      Hecho hecho3 = new Hecho("Incendio en Patagonia", "Fuego en bosque", "Incendio forestal", -41.1335, -71.3103, LocalDate.of(2025, 1, 20), LocalDate.of(2025, 1, 21), Origen.DATASET);

      HechosAlmacenados.agregarHecho(hecho1);
      HechosAlmacenados.agregarHecho(hecho2);
      HechosAlmacenados.agregarHecho(hecho3);

      Administrador admin = new Administrador();
      Coleccion coleccion = admin.crearColeccion("Incendios y Contaminación", "Hechos sobre incendios y contaminación", new Fuente(){}, new FiltroCategoria("Incendio forestal"));


      // Act - Aplico un filtro para quedarme solo con "Incendio forestal"
      Filtro filtro = new FiltroCategoria("Incendio forestal");
      Filtro filtro2 = new FiltroFecha(LocalDate.of(2025, 2, 10));
      List<Hecho> filtrados = coleccion.filtrarHechos(filtro2);

      // Assert - Verifico que obtengo 2 hechos y que son los correctos
      assertEquals(1, filtrados.size());
      assertFalse(filtrados.contains(hecho1));
      assertTrue(filtrados.contains(hecho2));
      assertFalse(filtrados.contains(hecho3));
    }
}
*/