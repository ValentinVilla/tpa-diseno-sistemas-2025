package ar.edu.utn.frba.dds.concenso;

import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.FuenteDemo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlgoritmoConsensoTest {
  private AlgoritmoConsenso algoritmoConsenso;
  private final ArrayList<Fuente> fuentes =  new ArrayList<>();
  private Hecho hecho;


  @BeforeEach
  void setUp() {
    FuenteDinamica fuente1 = mock(FuenteDinamica.class);
    FuenteEstatica fuente2 = mock(FuenteEstatica.class);
    FuenteDemo fuente3 = mock(FuenteDemo.class);
    fuentes.add(fuente1);
    fuentes.add(fuente2);
    fuentes.add(fuente3);

    hecho = new HechoBuilder()
        .titulo("Inundación en Caballito")
        .descripcion("Calles anegadas tras fuertes lluvias")
        .categoria("Clima")
        .latitud(-34.618)
        .longitud(-58.442)
        .fechaAcontecimiento(LocalDateTime.of(2025, 8, 20, 23, 59, 59))
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CONTRIBUYENTE)
        .build();

  }

  @Test
  void multiplesMencionesTest() {
    algoritmoConsenso = AlgoritmoConsenso.MULTIPLES_MENCIONES;
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }

  @Test
  void multiplesMencionesNegativoTest() {
    algoritmoConsenso = AlgoritmoConsenso.MULTIPLES_MENCIONES;
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>());
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertFalse(hecho.estaConsensuado());
  }

  @Test
  void mayoriaSimpleTest() {
    algoritmoConsenso = AlgoritmoConsenso.MAYORIA_SIMPLE;
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }

  @Test
  void mayoriaSimpleNegativoTest() {
    algoritmoConsenso = AlgoritmoConsenso.MAYORIA_SIMPLE;
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>());
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertFalse(hecho.estaConsensuado());
  }

  @Test
  void absolutoTest() {
    algoritmoConsenso = AlgoritmoConsenso.MAYORIA_SIMPLE;
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }

  @Test
  void defaultTest() {
    algoritmoConsenso = AlgoritmoConsenso.DEFAULT;
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }
}