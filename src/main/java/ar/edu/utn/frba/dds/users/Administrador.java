package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.CriterioPertenencia;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.solicitudes.ListaDeSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.filtros.Filtro;

public class Administrador extends Contribuyente{
  public Coleccion crearColeccion(String titulo, String descripcion, Fuente fuente, Filtro criterio) {
    return new Coleccion.Builder()
        .titulo(titulo)
        .descripcion(descripcion)
        .fuente(fuente)
        .criterio(criterio)
        .build();
  }

  void aceptarSolicitud(SolicitudEliminacion solicitud) {
    ListaDeSolicitudes.instance().sacarSolicitud(solicitud);
    solicitud.getHecho().setVisible(false); //solicitud rechazada => hecho no visible
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud) {
    ListaDeSolicitudes.instance().sacarSolicitud(solicitud);
  }

}
