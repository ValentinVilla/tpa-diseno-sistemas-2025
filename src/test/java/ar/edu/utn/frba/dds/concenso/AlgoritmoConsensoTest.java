package ar.edu.utn.frba.dds.concenso;

import ar.edu.utn.frba.dds.consenso.ConsensoAbsoluto;
import ar.edu.utn.frba.dds.consenso.ConsensoDefault;
import ar.edu.utn.frba.dds.consenso.ConsensoMayoriaSimple;
import ar.edu.utn.frba.dds.consenso.ConsensoMultiplesMenciones;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.fuentes.fuenteProxy.FuenteDemo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
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
        .fechaAcontecimiento(LocalDate.of(2025, 8, 20))
        .fechaCarga(LocalDate.now())
        .origen(Origen.CONTRIBUYENTE)
        .visible(true)
        .build();

  }

  @Test
  void multiplesMencionesTest() {
    algoritmoConsenso = new ConsensoMultiplesMenciones();
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }

  @Test
  void multiplesMencionesNegativoTest() {
    algoritmoConsenso = new ConsensoMultiplesMenciones();
    // Solo una fuente lo menciona, no hay consenso
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>());
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertFalse(hecho.estaConsensuado());
  }

  @Test
  void mayoriaSimpleTest() {
    algoritmoConsenso = new ConsensoMayoriaSimple();
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }

  @Test
  void mayoriaSimpleNegativoTest() {
    algoritmoConsenso = new ConsensoMayoriaSimple();
    // Solo una fuente lo menciona, no hay mayoría
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>());
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>());
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertFalse(hecho.estaConsensuado());
  }

  @Test
  void absolutoTest() {
    algoritmoConsenso = new ConsensoAbsoluto();
    when(fuentes.get(0).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(1).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    when(fuentes.get(2).cargarHechos(any())).thenReturn(new ArrayList<>(List.of(hecho)));
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }

  @Test
  void defaultTest() {
    algoritmoConsenso = new ConsensoDefault();
    algoritmoConsenso.tieneConsenso(hecho, fuentes);
    assertTrue(hecho.estaConsensuado());
  }
}
