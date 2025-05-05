package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.fuentes.LectorCSV;
import ar.edu.utn.frba.dds.users.Administrador;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class LeerCSVTest {
  @Test
  void puedeLeerUnArchivoCSVYDevolverHechos() throws Exception {
    // Arrange: crear archivo temporal CSV
    Path tempFile = Files.createTempFile("hechos", ".csv");
    try (FileWriter writer = new FileWriter(tempFile.toFile())) {
      writer.write("Título,Descripción,Latitud,Longitud,Fecha del hecho\n");
      writer.write("Robo,Robo en esquina,-34.6037,-58.3816,2023-08-15\n");
      writer.write("Incendio,Incendio en edificio,-34.6038,-58.3820,2023-08-16\n");
    }

    // Act: leer el archivo
    LectorCSV lector = new LectorCSV();
    List<Hecho> hechos = lector.leerDesde(tempFile.toString(), "Emergencia")
        .toList();

    // Assert: verificar los datos leídos
    assertEquals(2, hechos.size());

    Hecho primero = hechos.get(0);
    assertEquals("Robo", primero.getTitulo());
    assertEquals("Robo en esquina", primero.getDescripcion());
    assertEquals("Emergencia", primero.getCategoria());
    assertEquals(-34.6037, primero.getLatitud());
    assertEquals(-58.3816, primero.getLongitud());
    assertEquals(LocalDate.of(2023, 8, 15), primero.getFechaAcontecimiento());
    assertEquals(LocalDate.now(), primero.getFechaCarga());

    Hecho segundo = hechos.get(1);
    assertEquals("Incendio", segundo.getTitulo());
  }
}

