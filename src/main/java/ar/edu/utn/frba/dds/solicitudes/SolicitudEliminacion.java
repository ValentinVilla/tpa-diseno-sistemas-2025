package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;

public class SolicitudEliminacion extends Solicitud {


  public SolicitudEliminacion(String textoFundamentacion, Hecho hecho, DetectorDeSpam detector) {
    super(hecho, textoFundamentacion, detector );

  }

  @Override
  public void aplicarRechazo(){
    return;
  }
  @Override
  public void aplicarAceptacion(){
    hecho.setVisible(false);
  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }


}