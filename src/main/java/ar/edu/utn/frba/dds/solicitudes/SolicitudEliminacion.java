package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;

public class SolicitudEliminacion extends Solicitud {
  private final String textoFundamentacion;
//  private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
//  private final Hecho hecho;

  public SolicitudEliminacion(String textoFundamentacion, Hecho hecho) {
    super(hecho);
    this.textoFundamentacion = textoFundamentacion;
  }

  @Override
  public void aplicarRechazo(){
    return;
  }
  @Override
  public void aplicarAceptacion(){
    hecho.setVisible(false);
  }

//  public void aceptar(Hecho hecho) {
//    this.estado = EstadoSolicitud.ACEPTADA;
//    hecho.setVisible(false);
//  }
//
//  public void rechazar() {
//    this.estado = EstadoSolicitud.RECHAZADA;
//  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }

  public String getFundamentacion() {
    return textoFundamentacion;
  }

  public Hecho getHecho() {
    return hecho;
  }

  public EstadoSolicitud getEstado() {
    return estado;
  }

}