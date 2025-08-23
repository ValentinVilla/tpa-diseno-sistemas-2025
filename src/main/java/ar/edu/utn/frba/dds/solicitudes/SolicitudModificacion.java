package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;


public class SolicitudModificacion extends Solicitud{
  Hecho hechoModificado;

  public SolicitudModificacion(Hecho hecho, String sugerenciaModificacion, DetectorDeSpam detector, Hecho hechoModificado) {
    super(hecho, sugerenciaModificacion, detector);
    this.hechoModificado = hechoModificado;
    hechoModificado.setVisible(false);
  }


  @Override
  public void aplicarAceptacion() {
    hechoModificado.setVisible(true);
    hecho.setVisible(false);
  }

  @Override
  public void aplicarRechazo() {

    //hecho.setVisible(true);
    //hechoModificado.setVisible(false);
    //notificarAlAutorDelRechazoDeModificacion();
  }

  public void aceptarConSugerencia(String sugerencia){
    aplicarAceptacion();
    textoFundamentacion = sugerencia;
    //generarSugerenciaDeModificacion(); esto en teoria tiene que notificar a quien realizo la modificacion
    System.out.println("Solicitud de modificación aceptada con sugerencia: " + textoFundamentacion);
  }
}
