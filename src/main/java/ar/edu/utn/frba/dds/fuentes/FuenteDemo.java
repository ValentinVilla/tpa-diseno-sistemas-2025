package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.clientes.ClienteDemo;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

public class FuenteDemo implements FuenteRemota {
  protected ClienteDemo cliente; //q es protected???
  // aca estoy usando la api externa q me da el clienteDemo

  public FuenteDemo() {
    this.cliente = new ClienteDemo();
  }

  @Override
  public void procesarHechosDesde(String urlBase, ParametrosConsulta parametros, Consumer<Hecho> procesador) { //parametros?? consummer??

    DateTime fechaUltimaConsulta = LocalDate.now().minusDays(1); // ejemplo


    Map<String, Object> datos;
    do {
      datos = cliente.siguienteHecho(urlBase, fechaUltimaConsulta);
      if (datos != null) {
        procesador.accept(cliente.adaptador.desdeMapa(datos));
      }
    } while (datos != null);
  }

//  @Override
//  public void procesarHechosColeccionDesde(String urlBase, String idColeccion, ParametrosConsulta parametros, Consumer<Hecho> procesador) {
//    List<Hecho> hechos = cliente.obtenerHechosColeccion(urlBase, idColeccion, parametros);
//    hechos.forEach(procesador);
//  }
}