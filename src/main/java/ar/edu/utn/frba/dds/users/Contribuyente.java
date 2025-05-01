package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.solicitudes.ListaDeSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

public class Contribuyente extends Visualizador{
  public void solicitarEliminacion(Hecho hecho, String textoFundamentacion) {
    SolicitudEliminacion solicitud = new SolicitudEliminacion(hecho, textoFundamentacion);
    ListaDeSolicitudes.instance().agregarSolicitud(solicitud);
  }
}
