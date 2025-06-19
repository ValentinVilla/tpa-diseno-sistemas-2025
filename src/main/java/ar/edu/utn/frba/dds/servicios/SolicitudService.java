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
    if (detector.esSpam(solicitud.getTextoFundamentacion())) {
      solicitud.rechazar();
    }

    hecho.agregarSolicitud(solicitud);
    repositorio.actualizar(solicitud);
  }

  public void aprobarSolicitud(SolicitudEliminacion solicitud, Hecho hecho) {
    //revisar de donde se llama este metodo para corroborar si trae cargado un hecho
    //reversionar parametros (hecho no se esta usando)
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
