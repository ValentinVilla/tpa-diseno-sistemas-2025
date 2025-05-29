package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;

public class SolicitudEliminacion {
  private final String textoFundamentacion;
  private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
  private final Hecho hecho;

  public SolicitudEliminacion(String textoFundamentacion, Hecho hecho) {
    this.textoFundamentacion = textoFundamentacion;
    this.hecho = hecho;
  }

  public Hecho getHecho() {
    return hecho;
  }

  public void aceptar(Hecho hecho) {
    this.estado = EstadoSolicitud.ACEPTADA;
    hecho.setVisible(false);
  }

  public void rechazar() {
    this.estado = EstadoSolicitud.RECHAZADA;
  }

  public boolean estaPendiente() {
    return estado == EstadoSolicitud.PENDIENTE;
  }

  public String getFundamentacion() {
    return this.textoFundamentacion;
  }
}