package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.fuentes.fuenteProxy.FuenteMetaMapa;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.clientes.ClienteMetaMapa;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FuenteMetaMapaTest {
  private ClienteMetaMapa cliente;
  private FuenteMetaMapa fuente;
  private ParametrosConsulta parametros;

  private HechoBuilder crearHecho(String titulo) {
    return new HechoBuilder()
        .titulo(titulo)
        .descripcion("desc")
        .categoria("cat")
        .latitud(1.0)
        .longitud(1.0)
        .fechaAcontecimiento(LocalDate.now())
        .fechaCarga(LocalDate.now())
        .visible(true)
        .origen(Origen.CONTRIBUYENTE);
  }

  @BeforeEach
  void setUp() {
    cliente = mock(ClienteMetaMapa.class);
    fuente = new FuenteMetaMapa("http://example.com") {{
      this.cliente = FuenteMetaMapaTest.this.cliente;
    }};
    parametros = mock(ParametrosConsulta.class);
  }

  @Test
  void testCargarHechosConColeccionId() {
    when(parametros.getColeccionId()).thenReturn(Long.valueOf("123"));
    List<Hecho> hechosMock = List.of(new Hecho(crearHecho("Test 1.1")), new Hecho(crearHecho("Test 1.2")));

    when(cliente.obtenerHechosColeccion("http://example.com", "123", parametros))
        .thenReturn(hechosMock);

    ArrayList<Hecho> hechos = fuente.cargarHechos(parametros);

    assertEquals(2, hechos.size());
    verify(cliente, times(1))
        .obtenerHechosColeccion("http://example.com", "123", parametros);
  }

  @Test
  void testCargarHechosSinColeccionId() {
    when(parametros.getColeccionId()).thenReturn(null);
    List<Hecho> hechosMock = List.of(new Hecho(crearHecho("Test 2")));

    when(cliente.obtenerHechos("http://example.com", parametros))
        .thenReturn(hechosMock);

    ArrayList<Hecho> hechos = fuente.cargarHechos(parametros);

    assertEquals(1, hechos.size());
    verify(cliente, times(1))
        .obtenerHechos("http://example.com", parametros);
  }

  @Test
  void testEnviarSolicitudEliminacion() {
    SolicitudEliminacion solicitud = mock(SolicitudEliminacion.class);

    fuente.enviarSolicitudEliminacion("http://example.com", solicitud);

    verify(cliente, times(1))
        .enviarSolicitudEliminacion("http://example.com", solicitud);
  }

}