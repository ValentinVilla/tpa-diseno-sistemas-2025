package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("MODIFICACION")
public class SolicitudModificacion extends Solicitud{
  String valorHechoModificado;

  public SolicitudModificacion() {}

  public SolicitudModificacion(HechoDinamico hecho, String sugerenciaModificacion, DetectorDeSpam detector, Hecho hechoModificado) {
    super(hecho, sugerenciaModificacion, detector);
    this.valoresHecho =  hecho.getTitulo() + " | " + hecho.getDescripcion() + " | " + hecho.getCategoria();
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
