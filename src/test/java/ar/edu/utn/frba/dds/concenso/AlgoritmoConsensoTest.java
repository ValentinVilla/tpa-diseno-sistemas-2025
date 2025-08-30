package ar.edu.utn.frba.dds.concenso;

import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.FuenteDemo;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.fuentes.FuenteEstatica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

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

    Hecho hechoBase = new HechoBuilder()
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
    //algoritmoConsenso = new ConsensoMultiplesMenciones();
    //boolean resultado = algoritmoConsenso.tieneConsenso(hecho, fuentes);
    //assertTrue(resultado);
  }

  @Test
  void mayoriaSimpleTest() {
    //algoritmoConsenso = new ConsensoMayoriaSimple(hecho, fuentes);
    //boolean resultado = algoritmoConsenso.tieneConsenso(hecho, fuentes);
    //assertTrue(resultado);
  }

  @Test
  void absolutoTest() {
   // algoritmoConsenso = new ConsensoAbsoluto(hecho, fuentes);
    // algoritmoConsenso.tieneConsenso(hecho, fuentes);

    assertTrue(hecho.estaConsensuado());
  }

  @Test
  void defaultTest() {
    //algoritmoConsenso = new ConsensoDefault(hecho, fuentes);
    //boolean resultado = algoritmoConsenso.tieneConsenso(hecho, fuentes);
    //assertTrue(resultado);
  }
}
