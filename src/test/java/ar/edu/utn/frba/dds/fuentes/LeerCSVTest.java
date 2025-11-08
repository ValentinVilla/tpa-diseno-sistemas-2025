package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.LectorCSV;
import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LeerCSVTest {

  @Test
  void puedeLeerUnArchivoCSVYDevolverHechos() throws Exception {
    Path tempFile = Files.createTempFile("hechos", ".csv");
    List<Hecho> hechos = getHechoList(tempFile);

    assertEquals(2, hechos.size());

    Hecho primero = hechos.get(0);
    assertEquals("Robo", primero.getTitulo());
    assertEquals("Robo en esquina", primero.getDescripcion());
    assertEquals("Emergencia", primero.getCategoria());
    assertEquals(-34.6037, primero.getLatitud());
    assertEquals(-58.3816, primero.getLongitud());

    Hecho segundo = hechos.get(1);
    assertEquals("Incendio", segundo.getTitulo());
  }

  private static List<Hecho> getHechoList(Path tempFile) throws IOException {
    try (FileWriter writer = new FileWriter(tempFile.toFile())) {
      writer.write("titulo_custom,desc_custom,lat,lon,fecha\n");
      writer.write("Robo,Robo en esquina,-34.6037,-58.3816,2025-09-09 00:47:32.474046\n");
      writer.write("Incendio,Incendio en edificio,-34.6038,-58.3820,2025-09-09 00:47:32.474046\n");
    }

    ArrayList<String> campos = new ArrayList<>(List.of(
        "titulo_custom", "desc_custom", "lat", "lon", "fecha"
    ));

    LectorCSV lector = new LectorCSV();
    return lector.leerDesde(tempFile.toString(), "Emergencia", campos);
  }

  @Test
  void administradorPuedeCrearUnaColeccion() throws IOException {
    ArrayList<String> campos = new ArrayList<>(List.of(
        "titulo","descripcion","latitud","longitud","fechaHecho"
    ));

    FiltroCategoria filtro = new FiltroCategoria("Emergencia");
    FuenteEstatica fuente = new FuenteEstatica("/home/berty/UTN/DSI/tpa-2025-04/Diagramas/hechos_argentina.csv", "Emergencia", campos);
    AlgoritmoConsenso algoritmo = AlgoritmoConsenso.DEFAULT;

    RepositorioColecciones repoColeccion = RepositorioColecciones.getInstancia();

    crearColeccion("Ambiente", "Descripción de la colección", fuente, filtro, algoritmo, repoColeccion);

    assertNotNull(repoColeccion.listarTodas());
  }

  @Test
  void hechoConTituloDuplicadoSobrescribeAlAnterior() throws Exception {
    Path tempFile = Files.createTempFile("hechos", ".csv");
    List<Hecho> hechos = getHechos(tempFile);

    assertEquals(1, hechos.size(), "Debe haber solo un hecho (el segundo sobreescribe al primero)");

    Hecho hechoFinal = hechos.get(0);
    assertEquals("robo", hechoFinal.getTitulo());
    assertEquals("Robo actualizado", hechoFinal.getDescripcion());
    assertEquals(-34.6000, hechoFinal.getLatitud());
  }

  @Test
  void cargarHechosDesdeArchivoCSV() {
    ArrayList<String> campos = new ArrayList<>(List.of(
        "titulo","descripcion","latitud","longitud","fechaHecho"
    ));
    FuenteEstatica fuente = new FuenteEstatica("/home/berty/UTN/DSI/tpa-2025-04/Diagramas/hechos_argentina.csv", "Testing con el berty", campos);
    ParametrosConsulta parametros = new ParametrosConsulta();
    fuente.cargarHechos(parametros);
  }

  private static List<Hecho> getHechos(Path tempFile) throws IOException {
    try (FileWriter writer = new FileWriter(tempFile.toFile())) {
      writer.write("Título,Descripción,Latitud,Longitud,Fecha del hecho\n");
      writer.write("Robo,Robo original,-34.6037,-58.3816,2025-09-09 00:29:45.561021\n");
      writer.write("robo,Robo actualizado,-34.6000,-58.3800,2025-09-09 00:29:45.561021\n");
    }

    ArrayList<String> campos = new ArrayList<>(List.of(
        "Título", "Descripción", "Latitud", "Longitud", "Fecha del hecho"
    ));

    FuenteEstatica fuente = new FuenteEstatica(tempFile.toString(), "Emergencia", campos);
    return fuente.cargarHechos(null);
  }

  public void crearColeccion(String titulo, String descripcion, Fuente fuente, Filtro criterio, AlgoritmoConsenso algoritmo, RepositorioColecciones repositorio) {
    Coleccion nueva = new ColeccionBuilder()
        .titulo(titulo)
        .descripcion(descripcion)
        .fuente(fuente)
        .criterio(criterio)
        .algoritmoConsenso(algoritmo)
        .build();
    repositorio.guardar(nueva);
  }
}