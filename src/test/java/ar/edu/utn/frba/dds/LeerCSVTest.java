package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.fuentes.FuenteProxy;
import ar.edu.utn.frba.dds.fuentes.FuenteRemota;
import ar.edu.utn.frba.dds.fuentes.LectorCSV;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.servicios.ColeccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class LeerCSVTest {
  @Test
  void puedeLeerUnArchivoCSVYDevolverHechos() throws Exception {
    // Arrange: crear archivo temporal CSV
    Path tempFile = Files.createTempFile("hechos", ".csv");
    try (FileWriter writer = new FileWriter(tempFile.toFile())) {
      writer.write("titulo_custom,desc_custom,lat,lon,fecha\n");
      writer.write("Robo,Robo en esquina,-34.6037,-58.3816,2023-08-15\n");
      writer.write("Incendio,Incendio en edificio,-34.6038,-58.3820,2023-08-16\n");
    }

    // Ingresamos los campos de interes
    ArrayList<String> campos = new ArrayList<>(List.of(
        "titulo_custom", "desc_custom", "lat", "lon", "fecha"
    ));

    // Act: leer el archivo
    LectorCSV lector = new LectorCSV();
    List<Hecho> hechos = lector.leerDesde(tempFile.toString(), "Emergencia", campos);

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

  @Test
  void administradorPuedeCrearUnaColeccion() throws IOException {
    // Arrange: crear archivo temporal CSV
    Path tempFile = Files.createTempFile("hechos", ".csv");
    try (FileWriter writer = new FileWriter(tempFile.toFile())) {
      writer.write("Título,Descripción,Latitud,Longitud,Fecha del hecho\n");
      writer.write("Robo,Robo en esquina,-34.6037,-58.3816,2023-08-15\n");
      writer.write("Incendio,Incendio en edificio,-34.6038,-58.3820,2023-08-16\n");
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    ArrayList<String> campos = new ArrayList<>(List.of(
        "Título", "Descripción", "Latitud", "Longitud", "Fecha del hecho"
    ));

    FiltroCategoria filtro = new FiltroCategoria("Emergencia");
    FuenteEstatica fuente = new FuenteEstatica(tempFile.toString(), "Emergencia", campos);

    RepositorioColecciones repoColeccion = new RepositorioColecciones();
    RepositorioHechos repoHechos = new RepositorioHechos();
    ColeccionService coleccionService = new ColeccionService(repoColeccion, repoHechos);

    coleccionService.crearColeccion("Ambiente", "Descripción de la colección", fuente, filtro);

    assertNotNull(repoColeccion.obtenerTodas());
  }

  @Test
  void hechoConTituloDuplicadoSobrescribeAlAnterior() throws Exception {
    // Arrange: crear archivo temporal CSV con dos hechos de mismo título
    Path tempFile = Files.createTempFile("hechos", ".csv");
    try (FileWriter writer = new FileWriter(tempFile.toFile())) {
      writer.write("Título,Descripción,Latitud,Longitud,Fecha del hecho\n");
      writer.write("Robo,Robo original,-34.6037,-58.3816,2023-08-15\n");
      writer.write("robo,Robo actualizado,-34.6000,-58.3800,2023-09-01\n"); // mismo título, diferente casing
    }

    ArrayList<String> campos = new ArrayList<>(List.of(
        "Título", "Descripción", "Latitud", "Longitud", "Fecha del hecho"
    ));

    FuenteEstatica fuente = new FuenteEstatica(tempFile.toString(), "Emergencia", campos);
    List<Hecho> hechos = fuente.cargarHechos();

    assertEquals(1, hechos.size(), "Debe haber solo un hecho (el segundo sobreescribe al primero)");

    Hecho hechoFinal = hechos.get(0);
    assertEquals("robo", hechoFinal.getTitulo()); // el título del segundo (último leído)
    assertEquals("Robo actualizado", hechoFinal.getDescripcion());
    assertEquals(LocalDate.of(2023, 9, 1), hechoFinal.getFechaAcontecimiento());
    assertEquals(-34.6000, hechoFinal.getLatitud());
  }
}