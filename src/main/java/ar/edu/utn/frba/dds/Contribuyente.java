package ar.edu.utn.frba.dds;

public class Contribuyente {
  public void solicitarEliminacion(Hecho hecho, String textoFundamentacion) {
    SolicitudEliminacion solicitud = new SolicitudEliminacion(hecho, textoFundamentacion);
    ListaDeSolicitudes.instance().agregarSolicitud(solicitud);
  }
}
