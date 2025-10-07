package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;

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
    RepositorioHechos.getInstancia().actualizarVisibilidadPorTexto(valoresHecho, true);

    //notificarAlAutorDeLaAceptacionDeModificacion();
  }

  @Override
  public void aplicarRechazo() {
    //Si solo los dejamos no visibles no haria nada, si los eliminamos, removeria los hechos de la funte dinamica
    //notificarAlAutorDelRechazoDeModificacion();
  }

}
