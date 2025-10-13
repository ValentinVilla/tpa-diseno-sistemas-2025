package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("MODIFICACION")
public class SolicitudModificacion extends Solicitud{
  String valorHechoModificado;

  public SolicitudModificacion() {}

  public SolicitudModificacion(Hecho hecho, String sugerenciaModificacion, DetectorDeSpam detector, HechoDinamico hechoModificado) {
    super(hecho, sugerenciaModificacion, detector);
    this.valoresHecho =  hecho.getTitulo() + " | " + hecho.getDescripcion() + " | " + hecho.getCategoria();
    this.valorHechoModificado =
        hechoModificado.getTitulo() + ";" + hechoModificado.getDescripcion() + ";" + hechoModificado.getCategoria() + ";" +
            hechoModificado.getLatitud() + ";" + hechoModificado.getLongitud() + ";" + hechoModificado.getFechaAcontecimiento()
            + hechoModificado.getProvincia();
    ;
  }


  @Override
  public void aplicarAceptacion() {
    DAOHechos daoHechos = DAOHechos.getInstancia();
    daoHechos.actualizarHechoModificado(valoresHecho, valorHechoModificado);
  }

  //TODO: si existiera sugerencia informarsela al usuario, no creemos que eso se deba persistir

}
