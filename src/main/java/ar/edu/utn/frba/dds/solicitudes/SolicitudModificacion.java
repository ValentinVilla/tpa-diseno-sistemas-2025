package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;


public class SolicitudModificacion extends Solicitud{
  String sugerenciaModificacion;
  Hecho hechoModificado;

  public SolicitudModificacion(Hecho hecho, Hecho hechoModificado) {
    super(hecho);
    this.hechoModificado = hechoModificado;
  }

  @Override
  public void aplicarAceptacion() {

    return;
  }

  @Override
  public void aplicarRechazo() {
    hecho.setVisible(true);
    hechoModificado.setVisible(false);
    //notificarAlAutorDelRechazoDeModificacion();
  }

  public void aceptarConSugerencia(String sugerencia){
    sugerenciaModificacion = sugerencia;
    //generarSugerenciaDeModificacion(); esto en teoria tiene que notificar a quien realizo la modificacion
    System.out.println("Solicitud de modificación aceptada con sugerencia: " + sugerenciaModificacion);
  }
}
