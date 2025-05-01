package ar.edu.utn.frba.dds.solicitudes;

import java.util.ArrayList;
import java.util.List;

public class ListaDeSolicitudes {
  private static final ListaDeSolicitudes INSTANCE = new ListaDeSolicitudes();

  private final List<SolicitudEliminacion> listaDeSolicitudesDeEliminacion;

  private ListaDeSolicitudes() {
    this.listaDeSolicitudesDeEliminacion = new ArrayList<>();
  }

  public static ListaDeSolicitudes instance() {
    return INSTANCE;
  }

  public void sacarSolicitud(SolicitudEliminacion solicitud) {
    this.listaDeSolicitudesDeEliminacion.remove(solicitud);
  }

  public void agregarSolicitud(SolicitudEliminacion solicitud) {
    this.listaDeSolicitudesDeEliminacion.add(solicitud);
  }
}
