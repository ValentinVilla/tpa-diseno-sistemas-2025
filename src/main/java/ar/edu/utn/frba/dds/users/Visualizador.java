package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.servicios.ColeccionService;

public class Visualizador {
  protected ColeccionService coleccionService;

  public void navegarUnaColeccion(Coleccion coleccion) {
    coleccionService.obtenerHechosDe(coleccion);
  }

  public void navegarUnaColeccionSegunUnFiltro(Coleccion coleccion, Filtro filtro) {
    coleccionService.obtenerHechosFiltradosDe(coleccion, filtro);
  }
}