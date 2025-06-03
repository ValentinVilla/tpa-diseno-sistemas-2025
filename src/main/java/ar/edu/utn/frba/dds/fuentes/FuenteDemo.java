package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.clientes.ClienteDemo;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FuenteDemo implements FuenteProxy {
  protected ClienteDemo cliente;
  String direccionApi; // aca estoy usando la api externa con la que se comunica el clienteDemo
  LocalDateTime ultimaConsutla; //q es protected???


  public FuenteDemo(ClienteDemo cliente, String direccionApi) {
    this.cliente = cliente;
    this.direccionApi = direccionApi;
    this.ultimaConsutla = null;
  }

  @Override
  public void procesarHechosDesde(String direccionApi, ParametrosConsulta parametros, Consumer<Hecho> procesador) { //parametros?? consummer??
    if (pasoUnaHoraDesdeUltimaConsulta()) { // Este if nc si irira, en realidad buscamos que se dispare la actualizacion de los hechos cada una hora pero hasta q sepamos como hacerlo quedara asi
      List<Hecho> hechos = cliente.traerHechos(direccionApi);
      hechos.forEach(procesador); //la accion que deberia hacer es la de mostrarlos.
      ultimaConsutla = LocalDateTime.now();
    } else {
      System.out.println("No se actualizaron los hechos, ya que no ha pasado una hora desde la última consulta.");
    }//provisional el printf
  }

  @Override
  public void procesarHechosColeccionDesde(String direccionApi, Coleccion coleccion, ParametrosConsulta parametros, Consumer<Hecho> procesador) {
    if (pasoUnaHoraDesdeUltimaConsulta()) {
      List<Hecho> hechos = cliente.traerHechos(direccionApi);
      List<Hecho> hechosFiltrados = hechos.stream()
          .filter(hecho -> hecho.perteneceAColeccion(coleccion))
          .collect(Collectors.toList());
      hechos.forEach(procesador); //la accion que deberia hacer es la de mostrarlos o guardarlos en el repositorio.
      ultimaConsutla = LocalDateTime.now();
    }
    else {
      System.out.println("No se actualizaron los hechos, ya que no ha pasado una hora desde la última consulta.");
    }//provisional el printf
  }

  boolean pasoUnaHoraDesdeUltimaConsulta(){
    return ultimaConsutla == null || LocalDateTime.now().isAfter(ultimaConsutla.plusHours(1));
  }
}