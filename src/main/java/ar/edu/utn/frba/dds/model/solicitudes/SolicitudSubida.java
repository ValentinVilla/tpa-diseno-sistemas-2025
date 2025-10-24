package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SUBIDA")
public class SolicitudSubida extends Solicitud{

  public SolicitudSubida() {}

  public SolicitudSubida(Hecho hecho, String descripcion, DetectorDeSpam detector) {
    super(hecho, descripcion, detector);
  }

  public void aplicarAceptacion(){
    DAOHechos.getInstancia().actualizarVisibilidadPorTexto(valoresHecho, true);
    RepositorioSolicitudes.getInstancia().actualizar(this);
  }

}
