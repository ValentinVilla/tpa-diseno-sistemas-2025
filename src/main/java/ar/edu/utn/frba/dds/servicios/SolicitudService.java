package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.*;

public class SolicitudService {
  private RepositorioSolicitudes repositorio;

  private DetectorDeSpam detector;

  public SolicitudService(RepositorioSolicitudes repositorio,DetectorDeSpam detector) {
    this.repositorio = repositorio;
    this.detector = detector;
  }

  public void procesarNuevaSolicitud(SolicitudEliminacion solicitud, Hecho hecho) {
    if (detector.esSpam(solicitud.getFundamentacion())) {
      solicitud.rechazar();
    }

    hecho.agregarSolicitud(solicitud);
    repositorio.actualizar(solicitud);
  }

  public void aprobarSolicitud(SolicitudEliminacion solicitud, Hecho hecho) {
    solicitud.aceptar(hecho);
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
