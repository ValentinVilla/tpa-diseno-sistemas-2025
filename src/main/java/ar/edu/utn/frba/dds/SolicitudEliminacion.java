package ar.edu.utn.frba.dds;

public class SolicitudEliminacion {
  private Hecho hecho;
  private String textoFundamentacion;
  //private EstadoSolicitud estado;

  public Hecho getHecho() {
    return hecho;
  }


  // Constructor
  public SolicitudEliminacion(Hecho hecho, String textoFundamentacion) {
    this.hecho = hecho;
    this.textoFundamentacion = textoFundamentacion;
  }

}
