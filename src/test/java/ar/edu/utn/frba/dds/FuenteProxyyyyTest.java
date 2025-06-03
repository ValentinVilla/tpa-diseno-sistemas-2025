package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.fuentes.FuenteProxyyyy;
import ar.edu.utn.frba.dds.fuentes.FuenteProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FuenteProxyyyyTest {
  private FuenteProxy fuenteRemota;
  private ParametrosConsulta parametros;
  private Hecho hechoEjemplo;

  @BeforeEach
  void setUp() {
    fuenteRemota = mock(FuenteProxy.class);
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

    FuenteProxyyyy fuente = new FuenteProxyyyy("http://url", fuenteRemota, parametros);
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

    FuenteProxyyyy fuente = new FuenteProxyyyy("http://url", fuenteRemota, parametros, "coleccion123");
    ArrayList<Hecho> hechos = fuente.cargarHechos();

    assertEquals(1, hechos.size());
    assertEquals(hechoEjemplo, hechos.get(0));
    verify(fuenteRemota).procesarHechosColeccionDesde(eq("http://url"), eq("coleccion123"), eq(parametros), any());
  }
}


