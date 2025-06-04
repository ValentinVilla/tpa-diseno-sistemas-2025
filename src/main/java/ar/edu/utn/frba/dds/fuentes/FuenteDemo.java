package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.clientes.ClienteDemo;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FuenteDemo extends FuenteProxy {
  protected ClienteDemo cliente;
  String direccionApi;
  LocalDateTime ultimaConsulta;
  List<Hecho> cacheHechos;

  public FuenteDemo(ClienteDemo cliente, String direccionApi) {
    this.cliente = cliente;
    this.direccionApi = direccionApi;
    this.ultimaConsulta = null;
    this.cacheHechos = new ArrayList<>();
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    if (pasoUnaHoraDesdeUltimaConsulta()) {
      cacheHechos = cliente.traerHechos(direccionApi);
      ultimaConsulta = LocalDateTime.now();
    }
    Stream<Hecho> stream = cacheHechos.stream();
    if (parametros.getColeccionId() != null) {
      stream = stream.filter(hecho -> hecho.perteneceAColeccionId(parametros.getColeccionId()));
    }

    return stream.collect(Collectors.toCollection(ArrayList::new));
  }

  boolean pasoUnaHoraDesdeUltimaConsulta() {
    return ultimaConsulta == null || LocalDateTime.now().isAfter(ultimaConsulta.plusHours(1));
  }
}