package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;

public class SolicitudSubida extends Solicitud{
  String sugerenciaModificacion;

  public SolicitudSubida(Hecho hecho) {
    super(hecho);
  }

  public void aplicarAceptacion(){
    return;
  }

  public void aceptarConSugerencia(String sugerencia){
    sugerenciaModificacion = sugerencia;
    //generarSugerenciaDeModificacion(); esto en teoria tiene que notificar a quien realizo la modificacion
    System.out.println("Solicitud de modificación aceptada con sugerencia: " + sugerenciaModificacion);
  }

  public String getSugerenciaModificacion() {
    return sugerenciaModificacion;
  }

  @Override
  public void aplicarRechazo() {
    hecho.setVisible(false);
    //notificarAlAutorDelRechazoDeModificacion();
  }
}
