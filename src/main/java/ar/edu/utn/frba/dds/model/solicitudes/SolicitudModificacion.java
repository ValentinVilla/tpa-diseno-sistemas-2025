package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("MODIFICACION")
public class SolicitudModificacion extends Solicitud{
  @ManyToOne
  @JoinColumn(name = "hecho_modificado_id")
  Hecho hechoModificado;

  public SolicitudModificacion() {}

  public SolicitudModificacion(Hecho hecho, String sugerenciaModificacion, DetectorDeSpam detector, Hecho hechoModificado) {
    super(hecho, sugerenciaModificacion, detector);
    this.hechoModificado = hechoModificado;
    // En su lugar deberia sumarse al repo de eliminados?
    //hechoModificado.setVisible(false);
  }


  @Override
  public void aplicarAceptacion() {
    //hechoModificado.setVisible(true);
    //hecho.setVisible(false);
  }

  @Override
  public void aplicarRechazo() {
    /*
    //hecho.setVisible(true);
    //hechoModificado.setVisible(false);
    //notificarAlAutorDelRechazoDeModificacion();
    */
  }

  public void aceptarConSugerencia(String sugerencia){
    aplicarAceptacion();
    textoFundamentacion = sugerencia;
    //generarSugerenciaDeModificacion(); esto en teoria tiene que notificar a quien realizo la modificacion
    System.out.println("Solicitud de modificación aceptada con sugerencia: " + textoFundamentacion);
  }
}
