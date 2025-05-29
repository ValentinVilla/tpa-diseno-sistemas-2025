package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.fuentes.ClienteMetaMapa;
import ar.edu.utn.frba.dds.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuentes.FuenteProxy;
import ar.edu.utn.frba.dds.fuentes.FuenteRemota;
import ar.edu.utn.frba.dds.fuentes.LectorCSV;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.servicios.ColeccionService;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
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
import static org.mockito.Mockito.when;


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

    LectorCSV lector = new LectorCSV();
    List<Hecho> hechos = lector.leerDesde(tempFile.toString(), "Emergencia");

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
    Path tempFile = Files.createTempFile("hechos", ".csv");
    try (FileWriter writer = new FileWriter(tempFile.toFile())) {
      writer.write("Título,Descripción,Latitud,Longitud,Fecha del hecho\n");
      writer.write("Robo,Robo en esquina,-34.6037,-58.3816,2023-08-15\n");
      writer.write("Incendio,Incendio en edificio,-34.6038,-58.3820,2023-08-16\n");
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    FiltroCategoria filtro = new FiltroCategoria("Emergencia");
    FuenteEstatica fuente = new FuenteEstatica(tempFile.toString(), "Emergencia");

    RepositorioColecciones repoColeccion = new RepositorioColecciones();
    RepositorioHechos repoHechos = new RepositorioHechos();
    ColeccionService coleccionService = new ColeccionService(repoColeccion, repoHechos);

    coleccionService.crearColeccion("Ambiente", "Descripción de la colección", fuente, filtro);

    assertNotNull(repoColeccion.obtenerTodas());
  }
}

class FuenteProxyTest {
  private FuenteRemota fuenteRemota;
  private ParametrosConsulta parametros;
  private Hecho hechoEjemplo;

  @BeforeEach
  void setUp() {
    fuenteRemota = mock(FuenteRemota.class);
    parametros = mock(ParametrosConsulta.class);
    hechoEjemplo = new Hecho(new HechoBuilder());
  }

  @Test
  void testCargarHechosSinColeccion() {
    doAnswer(invocation -> {
      Consumer<Hecho> consumer = invocation.getArgument(2);
      consumer.accept(hechoEjemplo);
      return null;
    }).when(fuenteRemota).procesarHechosDesde(anyString(), any(), any());

    FuenteProxy fuente = new FuenteProxy("http://url", fuenteRemota, parametros);
    ArrayList<Hecho> hechos = fuente.cargarHechos();

    assertEquals(1, hechos.size());
    assertEquals(hechoEjemplo, hechos.get(0));
    verify(fuenteRemota).procesarHechosDesde(eq("http://url"), eq(parametros), any());

  }

  @Test
  void testCargarHechosConColeccion() {
    doAnswer(invocation -> {
      Consumer<Hecho> consumer = invocation.getArgument(3);
      consumer.accept(hechoEjemplo);
      return null;
    }).when(fuenteRemota).procesarHechosColeccionDesde(anyString(), anyString(), any(), any());

    FuenteProxy fuente = new FuenteProxy("http://url", fuenteRemota, parametros, "coleccion123");
    ArrayList<Hecho> hechos = fuente.cargarHechos();

    assertEquals(1, hechos.size());
    assertEquals(hechoEjemplo, hechos.get(0));
    verify(fuenteRemota).procesarHechosColeccionDesde(eq("http://url"), eq("coleccion123"), eq(parametros), any());
  }
}

class FuenteMetaMapaTest {
  private ClienteMetaMapa cliente;
  private FuenteMetaMapa fuente;
  private ParametrosConsulta parametros;

  @BeforeEach
  void setUp() {
    cliente = mock(ClienteMetaMapa.class);
    fuente = new FuenteMetaMapa() {
      { this.cliente = FuenteMetaMapaTest.this.cliente; } // Inyectar mock en clase anónima
    };
    parametros = mock(ParametrosConsulta.class);
  }

  @Test
  void testProcesarHechosDesde() {
    Hecho hecho = new Hecho(new HechoBuilder());
    when(cliente.obtenerHechos("http://url", parametros)).thenReturn(List.of(hecho));

    Consumer consumer = mock(Consumer.class);
    fuente.procesarHechosDesde("http://url", parametros, consumer);

    verify(cliente).obtenerHechos("http://url", parametros);
    verify(consumer).accept(hecho);
  }

  @Test
  void testProcesarHechosColeccionDesde() {
    Hecho hecho = new Hecho(new HechoBuilder());
    when(cliente.obtenerHechosColeccion("http://url", "abc123", parametros)).thenReturn(List.of(hecho));

    Consumer<Hecho> consumer = mock(Consumer.class);
    fuente.procesarHechosColeccionDesde("http://url", "abc123", parametros, consumer);

    verify(cliente).obtenerHechosColeccion("http://url", "abc123", parametros);
    verify(consumer).accept(hecho);
  }

  @Test
  void testEnviarSolicitudEliminacion() {
    SolicitudEliminacion solicitud = mock(SolicitudEliminacion.class);

    fuente.enviarSolicitudEliminacion("http://url", solicitud);
    verify(cliente).enviarSolicitudEliminacion("http://url", solicitud);
  }
}
