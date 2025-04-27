package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;


public class ListaDeSolicitudes {
  public List<SolicitudEliminacion> listaDeSolicitudesDeEliminacion;

  public void sacarSolicitud(SolicitudEliminacion solicitud) {
    this.listaDeSolicitudesDeEliminacion.remove(solicitud);

  }

  public void agregarSolicitud(SolicitudEliminacion solicitud) {
    this.listaDeSolicitudesDeEliminacion.add(solicitud);
  }

  // Constructor
  public ListaDeSolicitudes(List<SolicitudEliminacion> solicitudes) {
    this.listaDeSolicitudesDeEliminacion = new ArrayList<>();
  }
  
}
