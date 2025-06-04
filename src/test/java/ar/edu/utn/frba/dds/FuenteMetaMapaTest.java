package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.clientes.ClienteMetaMapa;
import ar.edu.utn.frba.dds.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FuenteMetaMapaTest {
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
