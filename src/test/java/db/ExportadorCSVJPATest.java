package db;

import ar.edu.utn.frba.dds.servicios.ExportadorCSV;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExportadorCSVJPATest {
  private static ExportadorCSV exportador;

  @BeforeAll
  static void setup() {
    exportador = new ExportadorCSV();
  }

  @AfterAll
  static void tearDown() {
    exportador.cerrar();
  }

  @Test
  void testExportarVistaACSV() throws IOException {
    String vista = "estadistica_categoria_top";
    String archivoSalida = "estadistica_test.csv";

    exportador.exportarVistaACSV(vista, archivoSalida);

    Path path = Paths.get(archivoSalida);
    assertTrue(Files.exists(path), "El archivo CSV debería existir");

    List<String> lineas = Files.readAllLines(path);
    assertFalse(lineas.isEmpty(), "El archivo CSV no debería estar vacío");

    String encabezado = lineas.get(0);
    assertTrue(encabezado.contains("id") && encabezado.contains("cantidad") && encabezado.contains("categoria"),
        "El encabezado debe contener los nombres de las columnas");

    Files.deleteIfExists(path);
  }
}
