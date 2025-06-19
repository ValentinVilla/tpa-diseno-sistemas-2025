package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;

public abstract class Solicitud {

  protected EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
  protected Hecho hecho;
  String sugerenciaModificacion;
  public DetectorDeSpam detector;

  public Solicitud(Hecho hecho, String sugerenciaModificacion, DetectorDeSpam detector) {
    this.hecho = hecho;
    this.estado = EstadoSolicitud.PENDIENTE;
    this.sugerenciaModificacion = sugerenciaModificacion;
    this.detector = detector;
    if(detector != null && detector.esSpam(sugerenciaModificacion)){
      throw new IllegalArgumentException("La sugerencia de modificación es spam");
    } //creo que tendriamos que implemetnar el "esSpam(textoSugerencia)" por si el texto de fundamentacion tiene 500 caracteres, enunciado E1.
  }

  public void aceptar(){
     this.estado = EstadoSolicitud.ACEPTADA;
     aplicarAceptacion();
  }
  public void rechazar(){
      this.estado = EstadoSolicitud.RECHAZADA;
      aplicarRechazo();
  }
 //metodos que implementa cada tipo de solicitud
  public abstract void aplicarAceptacion();
  public abstract void aplicarRechazo();
    // Implementar la lógica de aceptación


}
