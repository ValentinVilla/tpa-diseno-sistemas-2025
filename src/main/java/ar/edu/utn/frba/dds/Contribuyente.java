package ar.edu.utn.frba.dds;

public class Contribuyente {
  ListaDeSolicitudes solicitudes;
  private SolicitudEliminacion solicitudEliminacion;

  public void solicitarEliminacion(Hecho hecho, String textoFundamentacion, ListaDeSolicitudes solicitudes){
    SolicitudEliminacion solicitud = new SolicitudEliminacion(hecho, textoFundamentacion);
    solicitudes.agregarSolicitud(solicitud);
  }

}
