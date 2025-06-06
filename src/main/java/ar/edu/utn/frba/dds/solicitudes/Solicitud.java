package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;

public abstract class Solicitud {

  protected EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
  protected Hecho hecho;

  public Solicitud(Hecho hecho) {
    this.hecho = hecho;
    this.estado = EstadoSolicitud.PENDIENTE;
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
