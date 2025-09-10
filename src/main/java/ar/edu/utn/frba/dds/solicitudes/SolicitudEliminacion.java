package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ELIMINACION")
public class SolicitudEliminacion extends Solicitud {
  public SolicitudEliminacion() {
  }

  public SolicitudEliminacion(String textoFundamentacion, Hecho hecho, DetectorDeSpam detector) {
    super(hecho, textoFundamentacion, detector);
  }

  @Override
  public void aplicarRechazo() {
    this.estado = EstadoSolicitud.RECHAZADA;
  }

  @Override
  public void aplicarAceptacion() {
    hecho.setVisible(false);
    this.estado = EstadoSolicitud.ACEPTADA;
  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }
}