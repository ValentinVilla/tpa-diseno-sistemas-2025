package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.clientes.ClienteDemo;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FuenteDemoTest {

  private ClienteDemo cliente;
  private RepositorioColecciones repo;
  private FuenteDemo fuente;

  @BeforeEach
  void setUp() {
    cliente = mock(ClienteDemo.class);
    repo = mock(RepositorioColecciones.class);
    fuente = new FuenteDemo(cliente, "http://demo.com", repo);
  }

  @Test
  void siNoPasoUnaHoraNoActualizaCache() {
    List<Hecho> hechosMock = List.of(mock(Hecho.class));
    when(cliente.traerHechos("http://demo.com")).thenReturn(hechosMock);
    fuente.cargarHechos(new ParametrosConsulta());
    fuente.cargarHechos(new ParametrosConsulta());
    verify(cliente, times(1)).traerHechos("http://demo.com");
  }

  @Test
  void actualizaCacheSiPasoUnaHora() {
    List<Hecho> hechosMock1 = List.of(mock(Hecho.class));
    List<Hecho> hechosMock2 = List.of(mock(Hecho.class));
    when(cliente.traerHechos("http://demo.com"))
        .thenReturn(hechosMock1)
        .thenReturn(hechosMock2);

    fuente.cargarHechos(new ParametrosConsulta());
    // Simular que pasó una hora (dependiendo de la implementación, podrías exponer un método o mockear el reloj)
    fuente.forzarExpiracionCache(); //HACE QUE EL CACHE SE EXPIRE tiempo ult consulta = 2hs
    List<Hecho> hechos = fuente.cargarHechos(new ParametrosConsulta());

    assertEquals(hechosMock2, hechos);
    verify(cliente, times(2)).traerHechos("http://demo.com");
  }

/*  @Test
  void cargarHechosFiltraPorColeccion() {
    Hecho hecho = mock(Hecho.class);
    Coleccion coleccion = mock(Coleccion.class);
    ParametrosConsulta parametros = new ParametrosConsulta(
        null, null, null, null, null, null, 123L
    );
    when(cliente.traerHechos("http://demo.com")).thenReturn(List.of(hecho));
    when(repo.buscarPorHandle("123")).thenReturn(coleccion);
    when(coleccion.hechoPertenece(hecho)).thenReturn(true);
    List<Hecho> hechos = fuente.cargarHechos(parametros);
    assertEquals(1, hechos.size());
    assertTrue(hechos.contains(hecho));
  }
*/
  @Test
  void cargaHechosHastaQueNoHayMas() {
    List<Hecho> hechosMock = List.of(mock(Hecho.class), mock(Hecho.class));
    List<Hecho> hechosMock2 = List.of(mock(Hecho.class), mock(Hecho.class), mock(Hecho.class));
    when(cliente.traerHechos("http://demo.com")).thenReturn(hechosMock);
    List<Hecho> hechosCargados = fuente.cargarHechos(new ParametrosConsulta());
    assertEquals(2, hechosCargados.size());
    verify(cliente, times(1)).traerHechos("http://demo.com");
    fuente.forzarExpiracionCache();
    when(cliente.traerHechos("http://demo.com")).thenReturn(hechosMock2);
    hechosCargados = fuente.cargarHechos(new ParametrosConsulta());
    assertEquals(3, hechosCargados.size());
  }

}
