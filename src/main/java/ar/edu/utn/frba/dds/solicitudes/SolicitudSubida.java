package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


// ESTA CLASE SOLO SE USA EN LOS TESTS, NO EN LA LOGICA DE LA APP
// REVISAR NO SOLO ERRORES SINO PROPOSITO


@Entity
@DiscriminatorValue("SUBIDA")
public class SolicitudSubida extends Solicitud{

  public SolicitudSubida() {}

  public SolicitudSubida(Hecho hecho, String descripcion, DetectorDeSpam detector) {
    super(hecho, descripcion, detector);
  }

  public void aplicarAceptacion(){
    DAOHechos.getInstancia().actualizarVisibilidadPorTexto(valoresHecho, true);
  }

}
