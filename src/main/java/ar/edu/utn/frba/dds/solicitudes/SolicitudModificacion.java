package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;


public class SolicitudModificacion extends Solicitud{
  String sugerenciaModificacion;
  Hecho original;
  Hecho hechoModificado;

  public SolicitudModificacion(Hecho original, Hecho hechoModificado) {
    super(original);
    this.hechoModificado = hechoModificado;
    this.sugerenciaModificacion = sugerenciaModificacion;
  }

  @Override
  public void aplicarAceptacion() {

    return;
  }

  @Override
  public void aplicarRechazo() {
    hechoModificado.actualizarDesde(original);
    //notificarAlAutorDeLaModificacion();
    //actualizarDesde hace que el hecho que se actualiza se pone en visible false y al que esta dentro de los () actualiza la fecha de modificacion
  }

  public void aceptarConSugerencia(String sugerencia){
    sugerenciaModificacion = sugerencia;
    //generarSugerenciaDeModificacion(); esto en teoria tiene que notificar a quien realizo la modificacion
    System.out.println("Solicitud de modificación aceptada con sugerencia: " + sugerenciaModificacion);
  }
}
