package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechosEliminados;

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
  }

  @Override
  public void aplicarAceptacion() {
  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }
}