package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ELIMINACION")
public class SolicitudEliminacion extends Solicitud {
  public SolicitudEliminacion() {
  }

  public SolicitudEliminacion(Hecho hecho, String textoFundamentacion, DetectorDeSpam detector) {
    super(hecho, textoFundamentacion, detector);
  }

  @Override
  public void aplicarAceptacion() {
    DAOHechos.getInstancia().actualizarVisibilidadPorTexto(valoresHecho, false);
  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }
}