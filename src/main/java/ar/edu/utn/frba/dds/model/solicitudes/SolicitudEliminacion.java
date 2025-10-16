package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;

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
  public void aplicarAceptacion() {
    DAOHechos.getInstancia().actualizarVisibilidadPorTexto(valoresHecho, false);
  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }
}