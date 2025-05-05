package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.dominio.Hecho;

public class SolicitudEliminacion {
  private final Hecho hecho;
  private final String textoFundamentacion;
  private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

  public SolicitudEliminacion(Hecho hecho, String textoFundamentacion) {
    this.hecho = hecho;
    this.textoFundamentacion = textoFundamentacion;
  }

  public void aceptar() {
    this.estado = EstadoSolicitud.ACEPTADA;
    this.hecho.setVisible(false);
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

  public Hecho getHecho() {
    return this.hecho;
  }
}

