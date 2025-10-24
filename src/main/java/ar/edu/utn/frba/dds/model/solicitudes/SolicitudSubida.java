package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Arrays;

@Entity
@DiscriminatorValue("SUBIDA")
public class SolicitudSubida extends Solicitud{

  public SolicitudSubida() {}

  public SolicitudSubida(HechoDinamico hecho, String descripcion, DetectorDeSpam detector, Contribuyente contribuyente) {
    super(hecho, descripcion, detector, contribuyente);
  }

  public void aplicarAceptacion(){
    String[] partes = valoresHecho.split(";");
    String valoresHechoSinContribuyente = String.join(";", Arrays.copyOf(partes, partes.length - 1));

    DAOHechos.getInstancia().actualizarVisibilidadPorTexto(valoresHechoSinContribuyente, true);
    RepositorioSolicitudes.getInstancia().actualizar(this);
  }

  //todo: discutir que hacer con el hecho si se rechaza la subida (se elimina de la fuente?)

}
