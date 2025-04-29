package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.CriterioPertenencia;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.solicitudes.ListaDeSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.filtros.Filtro;

abstract class Usuario {

  public void navegarUnaColeccion(Coleccion coleccion) {
    coleccion.mostrarHechos();
  }

  public void navegarUnaColeccionSegunUnFiltro(Coleccion coleccion, Filtro filtro) {
    coleccion.mostrarHechosFiltrados(filtro);
  }
}