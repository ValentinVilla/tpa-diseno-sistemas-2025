package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.*;

public class SolicitudService {
  private RepositorioSolicitudes repositorio;

  public SolicitudService(RepositorioSolicitudes repositorio) {
    this.repositorio = repositorio;
  }

  public void aprobarSolicitud(SolicitudEliminacion solicitud) {
    solicitud.aceptar();
    repositorio.actualizar(solicitud);
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud) {
    solicitud.rechazar();
    repositorio.actualizar(solicitud);
  }

  public void guardar(SolicitudEliminacion solicitud) {
    repositorio.guardar(solicitud);
  }
}
