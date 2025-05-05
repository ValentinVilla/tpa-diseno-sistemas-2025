package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.servicios.SolicitudService;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

public class Contribuyente extends Visualizador{
  //debería tener más atributos de datos personales (diferencia con visualizador)
  protected SolicitudService solicitudService;

  public void solicitarEliminacion(Hecho hecho, String textoFundamentacion) {
    SolicitudEliminacion solicitud = new SolicitudEliminacion(hecho, textoFundamentacion);
    solicitudService.guardar(solicitud);
  }
}
