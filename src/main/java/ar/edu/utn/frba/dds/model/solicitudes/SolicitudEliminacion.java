package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
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
    this.estado = EstadoSolicitud.RECHAZADA;
  }

  @Override
  public void aplicarAceptacion() {
    RepositorioHechosEliminados repo = RepositorioHechosEliminados.getInstancia();
    //hay que pasar el tipo de hecho a un hecho eliminado antes de eliminarlo?
    //repo.agregarHechoEliminado(hecho);
    this.estado = EstadoSolicitud.ACEPTADA;
  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }
}