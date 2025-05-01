package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.filtros.Filtro;

public class Visualizador {
  public void navegarUnaColeccion(Coleccion coleccion) {
    coleccion.mostrarHechos();
  }

  public void navegarUnaColeccionSegunUnFiltro(Coleccion coleccion, Filtro filtro) {
    coleccion.mostrarHechosFiltrados(filtro);
  }
}