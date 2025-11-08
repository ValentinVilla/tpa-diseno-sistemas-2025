package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Arrays;

@Entity
@DiscriminatorValue("MODIFICACION")
public class SolicitudModificacion extends Solicitud{
  String valorHechoModificado;

  public SolicitudModificacion() {}

  public SolicitudModificacion(Hecho hecho, String sugerenciaModificacion, DetectorDeSpam detector, HechoDinamico hechoModificado, Contribuyente contribuyente) {
    super(hecho, sugerenciaModificacion, detector, contribuyente);
    this.valorHechoModificado =
        hechoModificado.getTitulo() + ";" + hechoModificado.getDescripcion() + ";" + hechoModificado.getCategoria() + ";" +
            hechoModificado.getLatitud() + ";" + hechoModificado.getLongitud() + ";" + hechoModificado.getFechaAcontecimiento()
            + hechoModificado.getProvincia();
    ;
  }


  @Override
  public void aplicarAceptacion() {
    String[] partesHecho = valoresHecho.split(";");
    DAOHechos daoHechos = DAOHechos.getInstancia();
    daoHechos.actualizarHechoModificado(partesHecho[0], partesHecho[1], partesHecho[2], valorHechoModificado);
  }

  //TODO: si existiera sugerencia informarsela al usuario, no creemos que eso se deba persistir

}
