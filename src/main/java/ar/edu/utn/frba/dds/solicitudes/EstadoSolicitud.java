package ar.edu.utn.frba.dds.solicitudes;

public enum EstadoSolicitud {
  PENDIENTE("pendiente"),
  ACEPTADA("aceptada"),
  RECHAZADA("rechazada");

  EstadoSolicitud(String estado) {
    this.estado = estado;
  }

  public String getEstado() {
    return estado;
  }
  String estado;
}
