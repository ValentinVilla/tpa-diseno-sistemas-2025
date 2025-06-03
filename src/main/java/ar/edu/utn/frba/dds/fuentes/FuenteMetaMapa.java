package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.clientes.ClienteMetaMapa;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

import java.util.List;
import java.util.function.Consumer;

public class FuenteMetaMapa implements FuenteProxy {
  protected ClienteMetaMapa cliente;

  public FuenteMetaMapa() {
    this.cliente = new ClienteMetaMapa();
  }

  @Override
  public void procesarHechosDesde(String urlBase, ParametrosConsulta parametros, Consumer<Hecho> procesador) {
    List<Hecho> hechos = cliente.obtenerHechos(urlBase, parametros);
    procesar(hechos, procesador);
  }

  public void procesarHechosColeccionDesde(String urlBase, Coleccion coleccion, ParametrosConsulta parametros, Consumer<Hecho> procesador) {
    List<Hecho> hechos = cliente.obtenerHechosColeccion(urlBase, coleccion.handle, parametros);
    procesar(hechos, procesador);
  }

  public void enviarSolicitudEliminacion(String urlBase, SolicitudEliminacion solicitud) {
    cliente.enviarSolicitudEliminacion(urlBase, solicitud);
  }

  private void procesar(List<Hecho> hechos, Consumer<Hecho> procesador) {
    for (Hecho h : hechos) procesador.accept(h);
  }
}