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
@DiscriminatorValue("ELIMINACION")
public class SolicitudEliminacion extends Solicitud {
  public SolicitudEliminacion() {
  }

  public SolicitudEliminacion(Hecho hecho, String justificacion , DetectorDeSpam detector, Contribuyente contribuyente) {
    super(hecho, justificacion, detector, contribuyente);
  }

  @Override
  public void aplicarAceptacion() {
    String[] partes = valoresHecho.split(";");
    String valoresHechoSinContribuyente = String.join(";", Arrays.copyOf(partes, partes.length - 1));

    DAOHechos.getInstancia().actualizarVisibilidadPorTexto(valoresHechoSinContribuyente, false);
    RepositorioSolicitudes.getInstancia().actualizar(this);
  }
}